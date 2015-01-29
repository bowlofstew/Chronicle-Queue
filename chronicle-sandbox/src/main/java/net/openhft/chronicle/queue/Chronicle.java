package net.openhft.chronicle.queue;

import net.openhft.lang.model.constraints.NotNull;

import java.io.Closeable;
import java.io.IOException;


/**
 * <em>Chronicle</em> (in a generic sense) is a Java project focused on building a persisted low latency messaging
 * framework for high performance and critical applications.
 *
 * <p> Using non-heap storage options <em>Chronicle</em> provides a processing environment where applications does not
 * suffer from <em>GarbageCollection</em>. <em>GarbageCollection</em> (GC) may slow down your critical operations
 * non-deterministically at any time.. In order to avoid non-determinism and escape from GC delays off-heap memory
 * solutions are addressed. The main idea is to manage your memory manually so does not suffer from GC. Chronicle
 * behaves like a management interface over off-heap memory so you can build your own solutions over it.
 *
 * <p><em>Chronicle</em> uses RandomAccessFiles while managing memory and this choice brings lots of possibility. Random
 * access files permit non-sequential, or random, access to a file's contents. To access a file randomly, you open the
 * file, seek a particular location, and read from or write to that file. RandomAccessFiles can be seen as "large"
 * C-type byte arrays that you can access any random index "directly" using pointers. File portions can be used as
 * ByteBuffers if the portion is mapped into memory.
 *
 * <p>{@link net.openhft.chronicle.Chronicle} (now in the specific sense) is the main interface for management and can
 * be seen as the "Collection class" of the <em>Chronicle</em> environment. You will reserve a portion of memory and
 * then put/fetch/update records using the {@link net.openhft.chronicle.Chronicle} interface.</p>
 *
 * <p>{@link net.openhft.chronicle.Excerpt} is the main data container in a {@link net.openhft.chronicle.Chronicle},
 * each Chronicle is composed of Excerpts. Putting data to a chronicle means starting a new Excerpt, writing data into
 * it and finishing the Excerpt at the end.</p>
 *
 * <p>While {@link net.openhft.chronicle.Excerpt} is a generic purpose container allowing for remote access, it also has
 * more specialized counterparts for sequential operations. See {@link net.openhft.chronicle.ExcerptTailer} and {@link
 * net.openhft.chronicle.ExcerptAppender}</p>
 *
 * @author peter.lawrey
 */
public interface Chronicle extends Closeable {
    /**
     * @return A descriptive name for this chronicle which can be used for logging.
     */
    String name();

    /**
     * An Excerpt can be used access entries randomly and optionally change them.
     */
    @NotNull
    Excerpt createExcerpt() throws IOException;

    /**
     * A Tailer can be used to read sequentially from the start of a given position.
     */
    @NotNull
    ExcerptTailer createTailer() throws IOException;

    /**
     * An Appender can be used to write new excerpts sequentially to the end.
     */
    @NotNull
    ExcerptAppender createAppender() throws IOException;

    /**
     * @return The current estimated number of entries.
     */
    long size();

    /**
     * Remove all the entries in the chronicle.
     */
    void clear();

    /**
     * @return the lowest valid index available.
     */
    long firstAvailableIndex();

    /**
     * @return the highest valid index immediately available.
     */
    long lastWrittenIndex();
}