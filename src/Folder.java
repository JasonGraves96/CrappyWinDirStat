
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
public class Folder {
    private int numberOfFiles;



    private double totalSize;
    private String name;
    private List<Folder> children;

    public Folder() {
        this.children = new ArrayList<>();

    }


    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public long getTotalSize() {
        return (long) totalSize;
    }

    public void setTotalSize(double totalSize) {
        this.totalSize = totalSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Folder> getChildren() {
        return children;
    }

    public void setChildren(List<Folder> children) {
        this.children = children;
    }

    public Folder(int numberOfFiles, double totalSize, String name, List<Folder> children) {
        this.numberOfFiles = numberOfFiles;
        this.totalSize = totalSize;
        this.name = name;
        this.children = children;
    }//auto generated constructor


    public void read(Path path) throws IOException {
        this.name = String.valueOf(path.getFileName());

        List<Path> entries = new ArrayList<>();



        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                entries.add(entry);
            }
        } catch (IOException | DirectoryIteratorException ex) {
            throw new IOException("Failed to read directory", ex);
        }

        for (int i = 0; i < entries.size(); i++) {
            Path entry = entries.get(i);
            if (Files.isDirectory(entry)) {
                // Handle subdirectory
                Folder subFolder = new Folder();
                subFolder.read(entry);
                try {
                    children.add(subFolder);
                }catch(NullPointerException npe){}
            } else if (Files.isRegularFile(entry)) {
                // Handle file
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                numberOfFiles++;
                totalSize += attrs.size();
            }
        }
    }


    @Override
    public String toString() {
        return "- "+name+" has "+numberOfFiles+
                " non folder files totaling "+totalSize+
                " data points and the following children"+"\n"+printAllFolderNames(0);

    }

    public String printAllFolderNames(int level) {
        StringBuilder result = new StringBuilder();
        String indent = " ".repeat(level+1 * 2); // Create indentation based on level

        for (Folder child : children) {
            result.append(indent)
                    .append("- ")
                    .append(child.getName())
                    .append(" (")
                    .append(child.getNumberOfFiles())
                    .append(" files, ")
                    .append(convertBytes(child.getTotalSize()))
                    .append(")\n");

            // Recursively print the subfolder names with increased indentation
            result.append(child.printAllFolderNames(level + 1));
        }
        return result.toString();
    }

    public static String convertBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else if (bytes < 1024L * 1024 * 1024 * 1024) {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        } else {
            return String.format("%.2f TB", bytes / (1024.0 * 1024 * 1024 * 1024));
        }
    }


}
