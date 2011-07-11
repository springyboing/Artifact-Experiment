package co.uk.accio.artifact;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class CpsContentArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "Content";

    public CpsContentArtefactHandler() {
        super(TYPE, GrailsCpsContentClass.class, DefaultGrailsCpsContentClass.class, null);
    }

    public boolean isArtefactClass(Class clazz) {
        // class shouldn't be null and shoud ends with Job suffix
        if(clazz == null || !clazz.getName().endsWith(DefaultGrailsCpsContentClass.CONTENT)) return false;
        // and should have one of execute() or execute(JobExecutionContext) methods defined
        Method method = ReflectionUtils.findMethod(clazz, DefaultGrailsCpsContentClass.EXECUTE);


//        if(method == null) {
//            // we're using Object as a param here to allow groovy-style 'def execute(param)' method
//            method = ReflectionUtils.findMethod(clazz, DefaultGrailsCpsContentClass.EXECUTE, new Class[]{Object.class});
//        }
//        if(method == null) {
//          // also check for the execution context as a variable because that's what's being passed
//          method = ReflectionUtils.findMethod(clazz, DefaultGrailsCpsContentClass.EXECUTE, new Class[]{JobExecutionContext.class});
//        }

        return method != null;
    }
}
