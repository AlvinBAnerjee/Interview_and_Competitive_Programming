package MachineCoding_LLD.LLD_Interview_Problems._04_Easy_LRUCache.policy;

import java.util.HashMap;
import java.util.Map;

/**
 * The heart of O(1) recency: a doubly-linked list of keys with a {@code key → node} index so any
 * node can be found, unlinked, or re-linked in constant time. The <b>head is the "keep" end</b>
 * (front) and the <b>tail is the "evict" end</b> (back).
 *
 * <ul>
 *   <li>{@link #addFirst} — a new key goes to the front.</li>
 *   <li>{@link #moveToFirst} — an accessed key jumps to the front (this is why an LRU <em>read</em>
 *       is actually a write).</li>
 *   <li>{@link #removeLast} — eviction pops the back (the least-recently kept key).</li>
 * </ul>
 *
 * All three are O(1): the index gives the node directly, and relinking is a handful of pointer
 * swaps. Package-private — it's the shared machinery behind {@link LruEvictionPolicy} and
 * {@link FifoEvictionPolicy}, which differ only in whether they call {@code moveToFirst} on access.
 */
final class LinkedKeyList<K> {

    private static final class Node<K> {
        final K key;
        Node<K> prev;
        Node<K> next;

        Node(K key) {
            this.key = key;
        }
    }

    private final Map<K, Node<K>> index = new HashMap<>();
    private Node<K> head; // front — most-recently kept
    private Node<K> tail; // back  — first to be evicted

    /** Insert a new key at the front. Caller guarantees the key isn't already present. */
    void addFirst(K key) {
        Node<K> node = new Node<>(key);
        index.put(key, node);
        linkFront(node);
    }

    /** Move an existing key to the front. No-op if the key is absent or already at the front. */
    void moveToFirst(K key) {
        Node<K> node = index.get(key);
        if (node == null || node == head) {
            return;
        }
        unlink(node);
        linkFront(node);
    }

    /** Remove and return the key at the back (eviction end), or {@code null} if empty. */
    K removeLast() {
        if (tail == null) {
            return null;
        }
        Node<K> victim = tail;
        unlink(victim);
        index.remove(victim.key);
        return victim.key;
    }

    boolean contains(K key) {
        return index.containsKey(key);
    }

    int size() {
        return index.size();
    }

    // ---- pointer plumbing (all O(1)) ----

    private void linkFront(Node<K> node) {
        node.prev = null;
        node.next = head;
        if (head != null) {
            head.prev = node;
        }
        head = node;
        if (tail == null) { // list was empty
            tail = node;
        }
    }

    private void unlink(Node<K> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next; // node was the head
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev; // node was the tail
        }
        node.prev = null;
        node.next = null;
    }
}
