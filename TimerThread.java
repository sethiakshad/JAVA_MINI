class TimerThread extends Thread {
    private int remainingSeconds;
    private volatile boolean timesUp = false;
    private volatile boolean stop = false;

    public TimerThread(int seconds) {
        this.remainingSeconds = seconds;
        this.setDaemon(true);
    }
    @Override
    public void run() {
        while (remainingSeconds >= 0 && !stop) {
            if (!Main.isPrintingUI) {
                System.out.print("\033[s\033[1B\r\033[K[ TIMER: " + remainingSeconds + "s ]\033[u");
                System.out.flush();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            remainingSeconds--;
        }
        if (remainingSeconds < 0 && !stop) {
            timesUp = true;
            System.out.println("\n\n=== TIME IS UP! Press ENTER to see results ===");
        }
    }
    public boolean isTimesUp() {
        return timesUp;
    }
    public void stopTimer() {
        this.stop = true;
        this.interrupt();
    }
}