package grailscrowd.core.message
/**
 * @author ap
 */
interface MailerJobConfigMBean {

    public int getStartDelayInMillis()

    public void setStartDelayInMillis(int value)

    public int getTimeoutInMillis()

    public void setTimeoutInMillis(int value)

    public boolean isJobSchedulingEnabled()

    public void disableJobScheduling()

    public void enableJobScheduling()

}