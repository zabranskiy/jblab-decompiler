package JavaPrinters

import pretty.*

import com.sdc.languages.general.printers.Printer
import com.sdc.languages.general.printers.OperationPrinter

import com.sdc.languages.general.languageParts.GeneralClass
import com.sdc.languages.general.languageParts.GeneralClass.ClassType.*
import com.sdc.languages.general.languageParts.Method
import com.sdc.languages.general.languageParts.ClassField

import com.sdc.languages.java.printers.JavaOperationPrinter

import com.sdc.languages.java.languageParts.JavaClass
import com.sdc.languages.java.languageParts.JavaMethod
import com.sdc.languages.java.languageParts.JavaClassField

import com.sdc.ast.expressions.identifiers.Variable


class JavaPrinter: Printer() {
    override fun getOperationPrinter(): OperationPrinter {
       return JavaOperationPrinter.getInstance() as OperationPrinter;
    }

    override fun printAnnotationIdentifier(): PrimeDoc = text("@")

    override fun printBaseClass(): PrimeDoc = text("Object")

    override fun printClass(decompiledClass: GeneralClass): PrimeDoc =
        when(decompiledClass.getType()){
            SIMPLE_CLASS -> printSimpleClass(decompiledClass)
            INTERFACE -> printSimpleClass(decompiledClass)
            ENUM -> printEnum(decompiledClass)
            ANNOTATION -> printSimpleClass(decompiledClass) //todo create printer for annotation
            else -> printSimpleClass(decompiledClass)
        }

    fun printSimpleClass(decompiledClass: GeneralClass): PrimeDoc {
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

    fun printEnum(decompiledClass: GeneralClass): PrimeDoc {
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

    override fun printMethod(decompiledMethod: Method): PrimeDoc {
        if (decompiledMethod.getName().equals(decompiledMethod.getDecompiledClass()?.getName())
            && decompiledMethod.getParameters()?.isEmpty() as Boolean && decompiledMethod.hasEmptyBody())
        {
            return nil()
        }
        var isStaticBlock:Boolean = decompiledMethod.getName()!!.contains("<clinit>");
        val classMethod: JavaMethod = decompiledMethod as JavaMethod

        var declaration : PrimeDoc = nil();
        if(isStaticBlock){
              declaration=text("static")
        } else{
            declaration = group(printAnnotations(classMethod.getAnnotations()!!.toList()) + text(classMethod.getModifier()))

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
            declaration = group(declaration + arguments + text(")") + throwsExceptions)
        }

        val nestedClasses = nest(classMethod.getNestSize(), printMethodInnerClasses(classMethod.getDecompiledClass(), classMethod.getName(), classMethod.getSignature()))

        val body = nest(
                       classMethod.getNestSize(),
                        printConstruction(classMethod.getBegin(), classMethod.getNestSize())
                       + printMethodError(classMethod)
                   ) / text("}")
        return  declaration +
            if(decompiledMethod.getModifier()?.contains("abstract") as Boolean ||
                decompiledMethod.getDecompiledClass()?.getType() == GeneralClass.ClassType.INTERFACE)
            {
                 text(";")
            } else {
                text(" {") + nestedClasses + body
            }
    }

    override fun printField(decompiledField: ClassField): PrimeDoc {
        val classField: JavaClassField = decompiledField as JavaClassField

        var fieldCode : PrimeDoc = text(classField.getModifier() + classField.getType() + classField.getName())
        if (classField.hasInitializer())
            fieldCode = fieldCode + text(" = ") + printExpression(classField.getInitializer(), classField.getNestSize())
        return fieldCode + text(";")
    }

    override fun printLastMethodParameter(variable: Variable, nestSize: Int): PrimeDoc {
        val myType = variable.getType()
        if(myType?.isArray() as Boolean){
            variable.declare()
            val typeWithoutOnePairOfBrackets = myType?.getTypeWithOnPairOfBrackets()?.toString(getOperationPrinter())
            return text(typeWithoutOnePairOfBrackets?.trim() + "... ") + printExpression(variable.getName(), nestSize)
        } else {
            return printExpression(variable, nestSize)
        }

    }
}