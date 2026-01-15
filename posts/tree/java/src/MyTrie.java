import java.util.*;

public class MyTrie {

    private class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord;

        public boolean isEmpty() {
            return children.isEmpty();
        }
    }

    private final TrieNode root;

    MyTrie() {
        root = new TrieNode();
    }

    public void insert(String s) {
        TrieNode current = root;
        for (char ch : s.toCharArray()) {
            current = current.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    public boolean search(String s) {
        TrieNode lastNode = getLastNode(s);
        return lastNode != null && lastNode.isEndOfWord;
    }

    public void delete(String s) {
        delete(root, s, 0);
    }

    private boolean delete(TrieNode node, String s, int index) {
        if (s.length() == index) {
            if (!node.isEndOfWord) return false;
            node.isEndOfWord = false;
            return node.isEmpty();
        }

        char ch = s.charAt(index);
        TrieNode childNode = node.children.get(ch);
        if (childNode == null) return false;

        boolean shouldDelete = delete(childNode, s, index + 1);

        if (shouldDelete) {
            node.children.remove(ch);
            return !node.isEndOfWord && node.isEmpty();
        }

        return false;
    }

    public boolean startsWith(String prefix) {
        return getLastNode(prefix) != null;
    }

    private TrieNode getLastNode(String s) {
        TrieNode current = root;
        for (char ch : s.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return null;
        }
        return current;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();

        TrieNode lastNode = getLastNode(prefix);
        if (lastNode != null) {
            findAllWords(lastNode, prefix, results);
        }

        return results;
    }

    private void findAllWords(TrieNode node, String s, List<String> results) {
        if (node.isEndOfWord) {
            results.add(s);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            findAllWords(entry.getValue(), s + entry.getKey(), results);
        }
    }
}
