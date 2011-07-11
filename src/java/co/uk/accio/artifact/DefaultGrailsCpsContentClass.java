package co.uk.accio.artifact;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;

public class DefaultGrailsCpsContentClass extends AbstractInjectableGrailsClass implements GrailsCpsContentClass {


    public static final String CONTENT = "Content";
    public static final String EXECUTE = "execute";

    public DefaultGrailsCpsContentClass(Class<?> clazz) {
		super(clazz, CONTENT);
    }

    public void execute() {
        getMetaClass().invokeMethod( getReference().getWrappedInstance(), EXECUTE, new Object[] {} );
	}
}
