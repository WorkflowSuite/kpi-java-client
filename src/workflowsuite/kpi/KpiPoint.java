package workflowsuite.kpi;

import org.jetbrains.annotations.Contract;

public final class KpiPoint {
    @Contract(pure = true)
    public String getCheckpointCode() {
        return checkpointCode;
    }

    void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }

    @Contract(pure = true)
    public int getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(int processTypeId) {
        this.processTypeId = processTypeId;
    }

    @Contract(pure = true)
    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    @Contract(pure = true)
    public boolean isBeforeActivity() {
        return isBeforeActivity;
    }

    public void setBeforeActivity(boolean beforeActivity) {
        isBeforeActivity = beforeActivity;
    }

    @Contract(pure = true)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Contract(pure = true)
    public String getRootPid() {
        return rootPid;
    }

    public void setRootPid(String rootPid) {
        this.rootPid = rootPid;
    }

    private String checkpointCode;
    private int processTypeId;
    private String activityCode;
    private boolean isBeforeActivity;
    private String pid;
    private String rootPid;
}
