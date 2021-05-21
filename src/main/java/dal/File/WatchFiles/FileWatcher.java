package dal.File.WatchFiles;

import dal.exception.DALexception;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 *
 */
public class FileWatcher {
   private HashSet<String> changedFiles;
   private HashSet<String> deletedFiles;
   private WatchService watchService;
   private Path directory;
   private WatchKey watchKey;


    {
        directory = Path.of("src/../Data/CSVData/");
        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchKey = directory.register(watchService,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
           throw new DALexception("Couldn't set watch service", e);
        }
    }

    public FileWatcher() throws DALexception {
        //on windows often this same updated file is added twice
        //so we use hashset
        changedFiles = new HashSet<>();
        //here to
        deletedFiles = new HashSet<>();
        startWatching();
    }

    /**
     * method is adding added or deleted files to the lists
     */
    private void startWatching() {
        Runnable runnable = () ->{
            while (true){
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path filePath = directory.resolve((Path) event.context());
                    if(event.kind() == ENTRY_DELETE)
                        deletedFiles.add(filePath.toString());
                    if(event.kind() == ENTRY_MODIFY) {
                        changedFiles.add(filePath.toString());
                        System.out.println(filePath.toString());
                        System.out.println("entry modified");
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private HashSet<String> getChangedFiles() {
        return changedFiles;
    }

    private HashSet<String> getDeletedFiles() {
        return deletedFiles;
    }

    public ChangesFiles getChanges(){
        HashSet<String> changedF = getChangedFiles();
        HashSet<String> deletedF = getDeletedFiles();
        ChangesFiles changesFiles = new ChangesFiles(changedF,
              deletedF);
        //later write another method to clear that
        return changesFiles;
    }
}
