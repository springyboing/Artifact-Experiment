import co.uk.accio.artifact.CpsContentArtefactHandler
import co.uk.accio.artifact.GrailsCpsContentClass
import org.springframework.context.ApplicationContext
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class ContentArtifactGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Your name"
    def authorEmail = ""
    def title = "Plugin summary/headline"
    def description = '''\\
Brief description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/content-artifact"

    def watchedResources = [
                "file:./grails-app/content/**/*Content.groovy",
                "file:./plugins/*/grails-app/content/**/*Content.groovy"
        ]

    def artefacts = [new CpsContentArtefactHandler()]



    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {

        application.contentClasses.each {cpsContentClass ->
            configureJobBeans.delegate = delegate
            configureJobBeans(cpsContentClass)
        }
    }

    def doWithDynamicMethods = { ctx ->

        println "doWithDynamicMethods()"

        application.contentClasses.each {GrailsCpsContentClass tc ->
            def mc = tc.metaClass
            def jobName = tc.getFullName()
            //def jobGroup = tc.getGroup()

            println "jobName: "+ jobName
            tc.execute()

        }
    }

    def doWithApplicationContext = {applicationContext ->
        application.contentClasses.each {jobClass ->
            scheduleJob.delegate = delegate
            scheduleJob(jobClass, applicationContext)
        }
    }

    def scheduleJob = {GrailsCpsContentClass jobClass, ApplicationContext ctx ->
        println "scheduleJob()"
    }

    def onChange = { event ->
        println "onChange()"
        if(application.isArtefactOfType(CpsContentArtefactHandler.TYPE, event.source)) {
            log.debug("Content ${event.source} changed. Reloading...")
            def context = event.ctx
        }
    }

    def onConfigChange = { event ->
        println "onConfigChange()"
    }

    def configureJobBeans = {GrailsCpsContentClass jobClass ->
        def fullName = jobClass.fullName

        "${fullName}Class"(MethodInvokingFactoryBean) {
            targetObject = ref("grailsApplication", true)
            targetMethod = "getArtefact"
            arguments = [CpsContentArtefactHandler.TYPE, jobClass.fullName]
        }

        "${fullName}"(ref("${fullName}Class")) {bean ->
            bean.factoryMethod = "newInstance"
            bean.autowire = "byName"
            bean.scope = "prototype"
        }

//        "${fullName}Detail"(JobDetailFactoryBean) {
//            name = fullName
//            group = jobClass.group
//            concurrent = jobClass.concurrent
//            volatility = jobClass.volatility
//            durability = jobClass.durability
//            if(jobClass.sessionRequired) {
//                jobListenerNames = ["${SessionBinderJobListener.NAME}"] as String[]
//            }
//        }
    }
}
