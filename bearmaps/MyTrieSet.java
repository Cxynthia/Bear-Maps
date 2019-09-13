
package bearmaps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrieSet implements TrieSet61BL {
    private TrieNode root;

    public MyTrieSet() {
        root = new TrieNode(false);
    }

    @Override
    /** Clears all items out of Trie */
    public void clear() {
        root = new TrieNode(false);
    }

    @Override
    /** Returns true if the Trie contains KEY, false otherwise */
    public boolean contains(String key) {
        TrieNode curr = root;
        /*if (key == null || key.length() < 1) {
            return false;
        }*/
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            // if we fall off the tree
            if (!curr.next.containsKey(c)) {
                return false;
            }
            // if the final node is not key
            if (i == key.length() - 1) {
                return curr.next.get(c).isKey;
                /*if (!curr.next.get(c).isKey) {
                    return false;
                } else {
                    return true;
                }*/
            }
            curr = curr.next.get(c);
        }
        return true;
    }

    @Override
    /** Inserts string KEY into Trie */
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        TrieNode curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.next.containsKey(c)) {
                curr.next.put(c, new TrieNode(c, false));
            }
            curr = curr.next.get(c);
        }
        curr.isKey = true;
    }

    @Override
    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        TrieNode curr = root;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (i == prefix.length() - 1 && curr.next.containsKey(c)) {
                curr = curr.next.get(c);
                for (Character a: curr.next.keySet()) {
                    colHelper(prefix + a, list, curr.next.get(a));
                }
                return list;
            }
            /*if (!curr.next.containsKey(c)) {
                return null;
            }*/
            curr = curr.next.get(c);
        }
        return list;
    }

    public void colHelper(String s, List<String> list, TrieNode n) {
        if (n.isKey) {
            list.add(s);
        }
        for (char c: n.next.keySet()) {
            colHelper(s + c, list, n.next.get(c));
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("Not Supported.");
    }

    public class TrieNode {
        private boolean isKey;
        private char ch;
        private Map<Character, TrieNode> next = new HashMap<Character, TrieNode>();

        TrieNode(Character c, boolean b) {
            ch = c;
            isKey = b;
        }

        TrieNode(boolean b) {
            isKey = b;
        }
    }
}
