package posts.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTrie {

    private static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord;

        public boolean isEmpty() {
            return children.isEmpty();
        }
    }

    private final TrieNode root;

    public MyTrie() {
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
        TrieNode current = root;
        TrieNode node = getLastNode(s);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String s) {
        return getLastNode(s) != null;
    }

    private TrieNode getLastNode(String s) {
        TrieNode current = root;
        for (char ch : s.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return null;
        }
        return current;
    }

    public void delete(String s) {
        delete(root, s, 0);
    }

    private boolean delete(TrieNode current, String s, int index) {
        if (s.length() == index) {
            if (!current.isEndOfWord) return false;
            current.isEndOfWord = false;
            return true;
        }

        char ch = s.charAt(index);
        TrieNode node = current.children.get(ch);
        if (node == null) return false;

        boolean shouldDeleteCurrentNode = delete(node, s, index + 1);
        if (shouldDeleteCurrentNode) {
            current.children.remove(ch);
            return !current.isEndOfWord && current.isEmpty();
        }

        return false;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode lastNode = getLastNode(prefix);
        if (lastNode != null) {
            findAllWords(lastNode, prefix, results);
        }
        return results;
    }

    private void findAllWords(TrieNode node, String currentWord, List<String> results) {
        if (node.isEndOfWord) {
            results.add(currentWord);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            findAllWords(entry.getValue(), currentWord + entry.getKey(), results);
        }
    }
}
