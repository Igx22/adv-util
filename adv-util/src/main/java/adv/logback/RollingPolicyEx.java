package adv.logback;

import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

// Logback 1.1.7
public class RollingPolicyEx<E> extends TimeBasedRollingPolicy<E> {

    String maxFileSizeAsString;

    @Override
    public void start() {
        TriggeringPolicyEx<E> sizeAndTimeBasedFNATP = new TriggeringPolicyEx<E>(); // THIS LINE CHANGED!!!
        if (maxFileSizeAsString == null) {
            addError("MaxFileSize property must be set");
            return;
        } else {
            addInfo("Achive files will be limied to [" + maxFileSizeAsString + "] each.");
        }

        sizeAndTimeBasedFNATP.setMaxFileSize(maxFileSizeAsString);
        setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);

        // most work is done by the parent
        super.start();
    }

    @Override
    public void rollover() throws RolloverFailure {
        try {
            super.rollover();
        } catch (NullPointerException ignored) {
            System.out.println();
            /*
иногда logback не готов к rollover на старте

java.lang.NullPointerException
	at ch.qos.logback.core.rolling.helper.FileFilterUtil.afterLastSlash(FileFilterUtil.java:46)
	at ch.qos.logback.core.rolling.TimeBasedRollingPolicy.rollover(TimeBasedRollingPolicy.java:162)
	at adv.logback.RollingPolicyEx.rollover(RollingPolicyEx.java:30)
	at ch.qos.logback.core.rolling.RollingFileAppender.attemptRollover(RollingFileAppender.java:204)
	at ch.qos.logback.core.rolling.RollingFileAppender.rollover(RollingFileAppender.java:183)
	at ch.qos.logback.core.rolling.RollingFileAppender.subAppend(RollingFileAppender.java:224)
	at ch.qos.logback.core.OutputStreamAppender.append(OutputStreamAppender.java:100)
	at ch.qos.logback.core.UnsynchronizedAppenderBase.doAppend(UnsynchronizedAppenderBase.java:84)
	at ch.qos.logback.core.spi.AppenderAttachableImpl.appendLoopOnAppenders(AppenderAttachableImpl.java:48)
	at ch.qos.logback.classic.Logger.appendLoopOnAppenders(Logger.java:270)
	at ch.qos.logback.classic.Logger.callAppenders(Logger.java:257)
	at ch.qos.logback.classic.Logger.buildLoggingEventAndAppend(Logger.java:421)
	at ch.qos.logback.classic.Logger.filterAndLog_0_Or3Plus(Logger.java:383)
	at ch.qos.logback.classic.Logger.log(Logger.java:765)
	at org.apache.commons.logging.impl.SLF4JLocationAwareLog.debug(SLF4JLocationAwareLog.java:131)
	at org.springframework.core.env.PropertySourcesPropertyResolver.getProperty(PropertySourcesPropertyResolver.java:81)
	at org.springframework.core.env.PropertySourcesPropertyResolver.getProperty(PropertySourcesPropertyResolver.java:60)
	at org.springframework.core.env.AbstractEnvironment.getProperty(AbstractEnvironment.java:531)
	at org.springframework.boot.context.config.DelegatingApplicationListener.getListeners(DelegatingApplicationListener.java:74)
	at org.springframework.boot.context.config.DelegatingApplicationListener.onApplicationEvent(DelegatingApplicationListener.java:56)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:166)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:138)
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:121)
	at org.springframework.boot.context.event.EventPublishingRunListener.publishEvent(EventPublishingRunListener.java:111)
	at org.springframework.boot.context.event.EventPublishingRunListener.environmentPrepared(EventPublishingRunListener.java:65)
	at org.springframework.boot.SpringApplicationRunListeners.environmentPrepared(SpringApplicationRunListeners.java:54)
	at org.springframework.boot.SpringApplication.createAndRefreshContext(SpringApplication.java:330)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:307)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1191)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1180)
	at biz.adv.ssp.SspStarter.main(SspStarter.java:21)
            * */
        }
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSizeAsString = maxFileSize;
    }

    @Override
    public String toString() {
        return "c.q.l.core.rolling.SizeAndTimeBasedRollingPolicy@" + this.hashCode();
    }
}
