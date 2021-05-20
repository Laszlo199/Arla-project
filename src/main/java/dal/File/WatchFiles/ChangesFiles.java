package dal.File.WatchFiles;

import java.util.HashSet;

/**
 *
 */
public class ChangesFiles {
    private HashSet<String> changedFiles;
    private HashSet<String> deletedFiles;

    public ChangesFiles(HashSet<String> changedFiles,
                        HashSet<String> deletedFiles) {
        this.changedFiles = changedFiles;
        this.deletedFiles = deletedFiles;
    }

    public HashSet<String> getChangedFiles() {
        return changedFiles;
    }

    public HashSet<String> getDeletedFiles() {
        return deletedFiles;
    }
}
