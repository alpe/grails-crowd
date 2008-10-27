package grailscrowd.core.message

/**
 * JMX interface to manage blacklist entries.
 * @author ap
 */
interface BlacklistMBean {

    /**
     * Add given regular expression to blacklist patterns.
     * @param regExp regular expression
     */
    public void addRegExpToBlacklist(String regExp)

    /**
     * Remove given regular expression from blacklist patterns.
     * @param regExp regular expression
     */
    public void removeRegExpFromBlacklist(String regExp)

    /**
     * Is given email address prohibited by any blacklist pattern?
     * @param emailAddressToCheck
     * @return result
     */
    public boolean isInBlacklist(String emailAddressToCheck)

    /**
     * Get list of all current blacklist elements.
     * @return result
     */
    public List getBlacklistElements()

}