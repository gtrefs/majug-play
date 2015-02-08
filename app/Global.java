import models.Entry;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {

	@Override
	public void onStart(Application appliction) {
		if(firstRun()){
			addSeedEntries();
		}
		super.onStart(appliction);
	}

	private boolean firstRun() {
		return Entry.isEmpty();
	}
	
	private void addSeedEntries() {
		saveEntry("First entry", "New blog. A lot of fun.");
		saveEntry("Second: Talk abut play", "A lot of technical details.");
	}

	private void saveEntry(String title, String message) {
		final Entry entry = new Entry(title, message);
		entry.save();
	}
}
