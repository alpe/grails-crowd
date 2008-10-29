package grailscrowd.core.message
/**
 * Internal message status.
 * @author ap
 */
enum MessageLifecycle{
    /* do not change order */
    NEW, SEEN, DELETED;

    /**
     * Is an allowed transition to given status.
     * <ul>
     * <li> Current to current</li>
     * <li> NEW to SEEN or DELETED</li>
     * <li> SEEN to DELETED/li>
     * </ul>
     * @return result
     */
    boolean canSwitchTo(MessageLifecycle destStatus){
        return this.ordinal()<=destStatus?.ordinal()
    }
}
