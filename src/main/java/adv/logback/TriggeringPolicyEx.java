package adv.logback;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;

// Logback 1.1.7
@NoAutoStart
public class TriggeringPolicyEx<E> extends SizeAndTimeBasedFNATP<E> {

    private boolean started = false;

    /*@Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        if (!started) {
            nextCheck = 0L;
            return started = true;
        }
        return super.isTriggeringEvent(activeFile, event);
    }*/

    @Override
    public void start() {
        super.start();
        nextCheck = 0L;
        isTriggeringEvent(null, null);
        try {
            tbrp.rollover();
        } catch (RolloverFailure e) {
            //Do nothing
        }
    }

    /*@Override
    public boolean isTriggeringEvent(File activeFile, final E event) {
        long time = getCurrentTime();
        if (time >= nextCheck) {
            Date dateInElapsedPeriod = dateInCurrentPeriod;
            elapsedPeriodsFileName = tbrp.fileNamePatternWCS
                    .convertMultipleArguments(dateInElapsedPeriod, currentPeriodsCounter);
            currentPeriodsCounter = 0;
            setDateInCurrentPeriod(time);
            computeNextCheck();
            return true;
        }

        // for performance reasons, check for changes every 16,invocationMask invocations
        if (((++invocationCounter) & invocationMask) != invocationMask) {
            return false;
        }
        if (invocationMask < 0x0F) {
            invocationMask = (invocationMask << 1) + 1;
        }

        if (activeFile.length() >= maxFileSize.getSize()) {
            elapsedPeriodsFileName = tbrp.fileNamePatternWCS
                    .convertMultipleArguments(new Date(), currentPeriodsCounter);
            currentPeriodsCounter++;
            return true;
        }

        return false;
    }*/

}
