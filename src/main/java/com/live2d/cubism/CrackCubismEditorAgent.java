package com.live2d.cubism;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrackCubismEditorAgent {

    private static final Logger logger = CECLogger.getLogger("");


    public static void premain(String agentArgs, Instrumentation inst) throws IOException {
        log("CubismEditorCrack Open!!!");

        inst.addTransformer(new CubismEditor_Transformer(), true);

    }


    public static void log(Object obj) {
        logger.log(Level.INFO, obj + "\n");
    }

    public static void log_ClassInject(String className) {
        log("Class " + "\"" + className + "\"" + " Inject...");
    }

    public static byte[] log_ClassCtMethods(String className, byte[] bytes) {
        String className_JS = className.replace("/", ".");

        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className_JS);

            CtMethod[] methods = ctClass.getMethods();

            for (CtMethod method : methods) {
                log(method.getLongName()+"||"+method.getMethodInfo());
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] setClassMethod(String className, Method_Date... date) {
        byte[] bytes = new byte[0];

        String className_JS = className.replace("/", ".");

        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className_JS);

            for (Method_Date d : date) {
                CtMethod method = ctClass.getMethod(d.methodName, d.methodDesc);
                method.setBody(d.body);
            }

            ctClass.detach();

            bytes = ctClass.toBytecode();

        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] setClassField(String className,Field_Date... date) {
        byte[] bytes = new byte[0];

        String className_JS = className.replace("/", ".");

        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className_JS);

            for (Field_Date d : date) {
                String desc = d.fieldDesc;
                CtField ctField;
                if (desc != null) ctField = ctClass.getField(d.fieldName, d.fieldDesc);
                else ctField = ctClass.getField(d.fieldName);
                ctField.setModifiers(d.set_modifier);
            }

            ctClass.detach();

            bytes = ctClass.toBytecode();

        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }


    public static byte[] setClassALL(String className, Method_Date[] methods,Field_Date[] fields) {
        byte[] bytes = new byte[0];

        String className_JS = className.replace("/", ".");

        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className_JS);

            for (Method_Date d : methods) {
                CtMethod method = ctClass.getMethod(d.methodName, d.methodDesc);
                method.setBody(d.body);
            }

            for (Field_Date d : fields) {
                CtField ctField = ctClass.getField(d.fieldName, d.fieldDesc);
                ctField.setModifiers(d.set_modifier);
            }

            ctClass.detach();

            bytes = ctClass.toBytecode();

        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static byte[] setNoClassByte(String className, Method_Date... date) {
        byte[] bytes = new byte[0];

        String className_JS = className.replace("/", ".");

        try {

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className_JS);

            for (Method_Date d : date) {
                CtMethod method = ctClass.getDeclaredMethod(d.methodName);
                method.setBody(d.body);
            }

            ctClass.detach();

            bytes = ctClass.toBytecode();

        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    final static String body_1 = """
            {
                this.a = com.live2d.c.j.c;
                return this.a;
            }
            """;

    final static Field_Date field_1 =
            Field_Date.getField_Date("a",null,Modifier.PRIVATE);

    final static Method_Date method_1 =
            Method_Date.getMethod_Date("a","()Lcom/live2d/c/j;",body_1);


    public static class CubismEditor_Transformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            switch (className) {
                case "com/live2d/c/k":
                    return setClassALL(className, new Method_Date[]{method_1}, new Field_Date[]{field_1});
                default:
                    // log(className);
            }
            return classfileBuffer;

        }
    }

    public static class Field_Date {
        public final String fieldName;
        public final String fieldDesc;
        public final int set_modifier;

        private Field_Date(String fieldName, String fieldDesc, int set_modifier) {
            this.fieldName = fieldName;
            this.fieldDesc = fieldDesc;
            this.set_modifier = set_modifier;
        }

        public static Field_Date getField_Date(String fieldName, String fieldDesc,int set_modifier){
            return new Field_Date(fieldName,fieldDesc, set_modifier);
        }
    }

    public static class Method_Date {

        public final String methodName;
        public final String methodDesc;
        public final String body;

        private Method_Date(String methodName, String methodDesc, String body) {
            this.methodName = methodName;
            this.methodDesc = methodDesc;
            this.body = body;
        }

        public static Method_Date getMethod_Date(String methodName, String methodDesc, String body) {
            return new Method_Date(methodName, methodDesc, body);
        }

        public static Method_Date getMethod_Date(String methodName, String body) {
            return new Method_Date(methodName, "", body);
        }

    }




}
