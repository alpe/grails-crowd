package grailscrowd.core.message
/**
 * @author ap
 */
interface MailerJobStatisticsMBean {

    public Date getJobStartTime()
    public Date getJobEndTime()
    public long getJobDurationInMillis()
    public int getSentMailsQuantity()
    public String getLastRecipients()
    public boolean isRunning()    

}

