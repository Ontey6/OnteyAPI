package ontey.api.filelog;

import lombok.NonNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class FileLog {
	
	private final File dataDirectory;
	
	private final Logger logger;
	
	public FileLog(@NonNull Logger logger, @NonNull File dataDirectory) {
		this.logger = logger;
		this.dataDirectory = dataDirectory;
		
		if(this.dataDirectory.exists() && !this.dataDirectory.isDirectory())
			throw new IllegalStateException("[OnteyAPI] FileLog directory already exists, but as a file: " + this.dataDirectory.getPath());
		
		if(!this.dataDirectory.exists())
			if(!this.dataDirectory.mkdirs())
				throw new IllegalStateException("[OnteyAPI] Could not generate the logs directory, disabling plugin");
	}
	
	public void saveStackTrace(@NonNull Throwable throwable) {
		for(int i = 0; i < 5; i++) {
			String name = throwable.getClass().getName() + "-" + UUID.randomUUID() + ".log";
			File file = new File(dataDirectory, name);
			
			if(!file.exists()) {
				writeStackTrace(throwable, file, name);
				logger.warn("An exception occurred, saved stack-trace to {}", name);
				return;
			}
		}
		
		logger.warn("Could not find a name for the FileLog {}", throwable.getClass().getName());
	}
	
	private void writeStackTrace(@NonNull Throwable throwable, @NonNull File file, @NonNull String name) {
		try(PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
			throwable.printStackTrace(pw);
		} catch(IOException ex) {
			logger.error("Could not write to error log '{}'", name);
		}
	}
}
