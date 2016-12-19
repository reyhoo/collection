public class ReadWriteLock {

	private int readingReaders = 0;
	private int writingWriters = 0;
	private int waitingWriters = 0;
	private boolean preferWriter = false;

	public synchronized void readLock() {
		while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		readingReaders++;

	}

	public synchronized void unReadLock() {
		readingReaders--;
		preferWriter = true;
		try {
			notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized void writeLock() {
		waitingWriters++;
		try {
			while (readingReaders > 0 || writingWriters > 0) {
				try {
					wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			waitingWriters--;
		}
		writingWriters++;
	}

	public synchronized void unWriteLock() {
		writingWriters --;
		preferWriter = false;
		try {
			notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
