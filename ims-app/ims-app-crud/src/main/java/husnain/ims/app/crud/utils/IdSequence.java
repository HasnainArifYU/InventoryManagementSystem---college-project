package husnain.ims.app.crud.utils;

import java.time.Instant;

/**
 * FUTURE ENHANCEMENT: This class should be replaced with the primary key auto
 * generation capabilities that come with DBMS implementations.
 * <p>
 * Generates a sequence of {@code int} values, which can be used as primary
 * keys/ids, for instance.
 * <p>
 * Starts the generation at {@code 1} and continues to add {@code 1} to that
 * value, ad infinitum, when its method {@link #next()} is called.
 *
 * @author Husnain Arif
 */
public class IdSequence {

    private int nextId;
    private int lastUpdated;

    private IdSequence() {
        nextId = 1;
        lastUpdated = Instant.now().getNano();
    }

    /**
     * Accesses an instance of {@link IdSequence} that is not expected to change
     * during the lifetime of the class.
     *
     * @return a instance of the {@link IdSequence} class.
     */
    public static IdSequence getInstance() {
        return IdSequenceHolder.INSTANCE;
    }

    /**
     * Generates the next value in the sequence.
     * <p>
     * Is {@code synchronized} so that clients accessing the method from
     * different threads will never get the same value.
     *
     * @return the next value in the sequence of {@code int} values.
     */
    public synchronized int next() {
        var now = Instant.now().getNano();

        if (now == lastUpdated) {
            now++;
        }

        lastUpdated = now;

        var id = nextId;

        nextId++;

        return id;
    }

    private static class IdSequenceHolder {

        private static final IdSequence INSTANCE = new IdSequence();
    }
}
