package org.spoofax.jsglr;

import java.util.Timer;
import java.util.TimerTask;

import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.ParseTimeoutException;
import org.spoofax.jsglr.shared.terms.ATermFactory;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SGLR extends org.spoofax.jsglr.client.SGLR {

    private static final Timer abortTimer = new Timer(true);

    private int abortTimerJobId;

    /**
     * Sets the maximum amount of time to try and parse a file, before a
     * {@link ParseTimeoutException} is thrown.
     * 
     * @param timeout
     *            The maximum time to parse, in milliseconds.
     */
    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    protected void initParseTimer() {
        if (timeout > 0) {
            // We use a single shared timer to conserve native threads
            // and a jobId to recognize stale abort events
            synchronized (abortTimer) {
                asyncAborted = false;
                ++abortTimerJobId;
            }
            final int jobId = abortTimerJobId;
            abortTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (abortTimer) {
                        if (abortTimerJobId == jobId)
                            asyncCancel();
                    }
                }
            }, timeout);
        } else {
            asyncAborted = false;
        }
    }

    public SGLR(ATermFactory pf, ParseTable parseTable) {
        super(pf, parseTable);
    }
}
