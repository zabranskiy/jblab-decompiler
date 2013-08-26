package JavaPrinter

import pretty.*
import com.sdc.java.JavaClass
import com.sdc.java.JavaClassField
import com.sdc.java.JavaMethod
import com.sdc.abstractLanguage.AbstractClass
import com.sdc.abstractLanguage.AbstractMethod
import com.sdc.abstractLanguage.AbstractClassField
import com.sdc.abstractLanguage.AbstractPrinter
import com.sdc.abstractLanguage.AbstractOperationPrinter
import com.sdc.java.JavaOperationPrinter
import com.sdc.ast.expressions.identifiers.Variable
import com.sdc.abstractLanguage.AbstractClass.ClassType.*

class JavaPrinter: AbstractPrinter() {
    override fun getOperationPrinter():AbstractOperationPrinter{
       return JavaOperationPrinter.getInstance() as AbstractOperationPrinter;
    }


    override fun printAnnotationIdentifier(): PrimeDoc = text("@")

    override fun printBaseClass(): PrimeDoc = text("Object")

    override fun printClass(decompiledClass: AbstractClass): PrimeDoc =
        when(decompiledClass.getType()){
            SIMPLE_CLASS -> printSimpleClass(decompiledClass)
            INTERFACE -> printSimpleClass(decompiledClass)
            ENUM -> printEnum(decompiledClass)
            ANNOTATION -> printSimpleClass(decompiledClass) //todo create printer for annotation
            else -> printSimpleClass(decompiledClass)
        }

    open fun printSimpleClass(decompiledClass: AbstractClass): PrimeDoc {
        val javaClass: JavaClass = decompiledClass as JavaClass

        var headerCode : PrimeDoc = printPackageAndImports(decompiledClass)

        var declaration : PrimeDoc = group(printAnnotations(javaClass.getAnnotations()!!.toList()) + text(javaClass.getModifier() + javaClass.getTypeToString() + javaClass.getName()))

        val genericsCode = printGenerics(javaClass.getGenericDeclaration())
        declaration = declaration + genericsCode

        val superClass = javaClass.getSuperClass()
        if (!superClass!!.isEmpty())
            declaration = group(declaration + nest(javaClass.getNestSize(), line() + text("extends " + superClass)))

        val implementedInterfaces = javaClass.getImplementedInterfaces()!!.toArray()
        if (!implementedInterfaces.isEmpty())
            declaration = group(
                    declaration
                    + nest(2 * javaClass.getNestSize(), line() + text("implements " + implementedInterfaces.get(0)))
            )
        for (interface in implementedInterfaces.drop(1)) {
            declaration = group(
                    (declaration + text(","))
                    + nest(2 * javaClass.getNestSize(), line() + text(interface as String))
            )
        }

        var javaClassCode : PrimeDoc = headerCode / group(declaration + text(" {")) + nest(javaClass.getNestSize(), printClassBodyInnerClasses(javaClass))

        for (classField in javaClass.getFields()!!.toList())
            javaClassCode = javaClassCode + nest(javaClass.getNestSize(), line() + printField(classField))

        for (classMethod in javaClass.getMethods()!!.toList())
            javaClassCode = javaClassCode / nest(javaClass.getNestSize(), line() + printMethod(classMethod))


        return javaClassCode / text("}")
    }

    open fun printEnum(decompiledClass: AbstractClass): PrimeDoc {
        val javaClass: JavaClass = decompiledClass as JavaClass

        var declaration : PrimeDoc = group(printAnnotations(javaClass.getAnnotations()!!.toList()) + text( javaClass.getModifier()+  javaClass.getTypeToString() + javaClass.getName()))

        val nestSize = javaClass.getNestSize()
        var fieldList = javaClass.getFields()!!.toList()
        val lastIndex = fieldList.size() - 1
        var argsDocs = fieldList.take(fieldList.size - 2)
                .map { arg -> text(arg.getName() + ", ") }
        var argumentsCode: PrimeDoc = fill(argsDocs)
        return group( declaration + text(" {") + nest(nestSize, line() + argumentsCode) + text(fieldList.get(lastIndex-1).getName())) / text("}")
    }

    override fun printMethod(decompiledMethod: AbstractMethod): PrimeDoc {
        if (decompiledMethod.getName().equals(decompiledMethod.getDecompiledClass()?.getName())
            && decompiledMethod.getParameters()?.isEmpty() as Boolean && decompiledMethod.hasEmptyBody())
        {
            return nil()
        }

        val classMethod: JavaMethod = decompiledMethod as JavaMethod

        var declaration : PrimeDoc = group(printAnnotations(classMethod.getAnnotations()!!.toList()) + text(classMethod.getModifier()))

        val genericsCode = printGenerics(classMethod.getGenericDeclaration())

        declaration = group(declaration + genericsCode + text(classMethod.getReturnType() + classMethod.getName() + "("))

        var throwsExceptions = group(nil())
        val exceptions = classMethod.getExceptions()
        if (!exceptions!!.isEmpty()) {
            throwsExceptions = group(text("throws " + exceptions.get(0)))
            for (exception in exceptions.drop(1)) {
                throwsExceptions = group((throwsExceptions + text(",")) / text(exception))
            }
            throwsExceptions = group(nest(2 * classMethod.getNestSize(), line() + throwsExceptions))
        }

        var arguments: PrimeDoc = printMethodParameters(classMethod)

        val nestedClasses = nest(classMethod.getNestSize(), printMethodInnerClasses(classMethod.getDecompiledClass(), classMethod.getName(), classMethod.getSignature()))

        val body = nest(
                       classMethod.getNestSize(),
                        printConstruction(classMethod.getBegin(), classMethod.getNestSize())
                       + printMethodError(classMethod)
                   ) / text("}")
        return group(declaration + arguments + text(")") + throwsExceptions) +
            if(decompiledMethod.getModifier()?.contains("abstract") as Boolean ||
                decompiledMethod.getDecompiledClass()?.getType() == AbstractClass.ClassType.INTERFACE)
            {
                 text(";")
            } else {
                text(" {") + nestedClasses + body
            }
    }

    override fun printField(decompiledField: AbstractClassField): PrimeDoc {
        val classField: JavaClassField = decompiledField as JavaClassField

        var fieldCode : PrimeDoc = text(classField.getModifier() + classField.getType() + classField.getName())
        if (classField.hasInitializer())
            fieldCode = fieldCode + text(" = ") + printExpression(classField.getInitializer(), classField.getNestSize())
        return fieldCode + text(";")
    }

    override fun printLastMethodParameter(variable: Variable, nestSize: Int): PrimeDoc {
        val myType = variable.getType()
        if(!myType!!.endsWith("[] ")){
            return printExpression(variable, nestSize)
        }
        return text(myType.substring(0, myType.size - 3) + "... ") + printExpression(variable.getName(), nestSize)
    }
}