package grailscrowd.core.message

/**
 * Transient Job statistics for monitoring purpose. 
 *
 * @author ap
 */
class MailerJobStatistics implements MailerJobStatisticsMBean {

    /** current job only*/
    private volatile def jobStatistic

    public void markJobStarted() {
        jobStatistic = new JobStatistic(startTime: System.currentTimeMillis())
    }

    public void markJobEnded() {
        jobStatistic.endTime = System.currentTimeMillis()
    }

    /**
     * {@inheritDoc}
     */
    public void addMailRecipient(String mailRecipient) {
        jobStatistic.addRecipient(mailRecipient)
    }

    /**
     * {@inheritDoc}
     */
    public Date getJobStartTime() {
        return new Date(jobStatistic.startTime)
    }

    /**
     * {@inheritDoc}
     */
    public Date getJobEndTime() {
        return new Date(jobStatistic.endTime)
    }

    /**
     * {@inheritDoc}
     */
    public long getJobDurationInMillis() {
        return isRunning() ? 0L : jobStatistic.endTime - jobStatistic.startTime
    }

    /**
     * {@inheritDoc}
     */
    public int getSentMailsQuantity() {
        jobStatistic.sentQuantity

    }

    /**
     * {@inheritDoc}
     */
    public String getLastRecipients() {
        return jobStatistic.recipients.join(', ')
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRunning() {
        return !jobStatistic.endTime
    }


}

/**
 * Common statistic values.
 * @author ap
 */
class JobStatistic {

    long startTime
    long endTime
    long quantity
    int sentQuantity
    final Set recipients = new HashSet()

    void addRecipient(String mailRecipient) {
        sentQuantity++
        recipients.add(mailRecipient)
    }

}