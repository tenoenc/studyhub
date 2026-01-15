import java.util.*;

/*
 * Trie (Prefix Tree)
 * - 문자열을 문자 단위로 분해하여 저장하며, 공통된 접두사를 공유하는 노드들로 구성된 트리입니다.
 * - 문자열 탐색, 사전 구축, 자동 완성 시스템 등 문자열 기반 검색 작업에서 탁월한 속도를 제공합니다.
 */
public class Trie {

    private static class TrieNode {
        // 1. 자식 노드들을 저장하는 맵입니다. 유니코드 문자를 모두 처리하기 위해 HashMap을 사용합니다.
        Map<Character, TrieNode> children = new HashMap<>();

        // 2. 현재 노드에서 하나의 완전한 단어가 끝나는지 나타내는 상태 값입니다.
        boolean isEndOfWord;

        // 3. 현재 노드 하위에 연결된 문자가 있는지 확인하여 삭제 시 메모리 정리 여부를 결정합니다.
        public boolean isEmpty() {
            return children.isEmpty();
        }
    }

    private final TrieNode root;

    public Trie() {
        // 1. 아무 문자도 담지 않은 빈 루트 노드를 생성하여 탐색의 시작점으로 삼습니다.
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        // 1. 삽입할 단어의 각 문자를 순서대로 하나씩 확인합니다.
        for (char ch : word.toCharArray()) {
            // 2. 현재 문자가 자식 노드에 없다면 새로 만들고, 있다면 기존 노드로 이동합니다.
            current = current.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        // 3. 단어의 마지막 문자가 위치한 노드에 '단어의 끝'임을 표시합니다.
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        // 1. 단어의 전체 경로를 따라가 마지막 문자의 노드를 가져옵니다.
        TrieNode node = getLastNode(word);
        // 2. 노드가 존재해야 하며, 그 지점이 실제 단어의 끝(isEndOfWord)으로 설정되어 있어야 합니다.
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        // 1. 입력된 접두사의 경로가 트라이 내부에 온전하게 존재하는지만 확인합니다.
        return getLastNode(prefix) != null;
    }

    private TrieNode getLastNode(String s) {
        TrieNode current = root;
        // 1. 문자열의 각 글자를 따라 자식 노드로 계속 이동합니다.
        for (char ch : s.toCharArray()) {
            current = current.children.get(ch);
            // 2. 중간에 연결된 문자가 없다면 해당 문자열은 존재하지 않는 것이므로 null을 반환합니다.
            if (current == null) return null;
        }
        // 3. 탐색을 마친 후 도달한 마지막 노드를 반환합니다.
        return current;
    }

    public void delete(String word) {
        // 1. 루트부터 시작하여 재귀적으로 단어를 찾아 삭제 로직을 수행합니다.
        delete(root, word, 0);
    }

    private boolean delete(TrieNode current, String word, int index) {
        // 1. 단어의 마지막 글자 노드에 도달한 경우의 처리입니다.
        if (index == word.length()) {
            // 1-1. 단어가 트라이에 등록되어 있지 않다면 삭제를 중단합니다.
            if (!current.isEndOfWord) return false;

            // 1-2. 단어의 끝 표시를 해제하여 논리적으로 단어를 삭제합니다.
            current.isEndOfWord = false;

            // 1-3. 만약 하위 자식 노드가 전혀 없다면 물리적으로 삭제가 가능함을 알립니다.
            return current.isEmpty();
        }

        // 2. 단어를 찾아 하위 노드로 내려가는 과정입니다.
        char ch = word.charAt(index);
        TrieNode node = current.children.get(ch);
        if (node == null) return false;

        // 3. 자식 노드에서 삭제 가능 여부를 재귀적으로 확인하며 올라옵니다.
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1);

        // 4. 하위 노드로부터 삭제 가능 신호(true)를 받았을 때의 처리입니다.
        if (shouldDeleteCurrentNode) {
            // 4-1. 부모 노드의 맵에서 자식 포인터를 실제로 제거합니다.
            current.children.remove(ch);
            // 4-2. 현재 노드도 다른 단어의 끝이 아니고 자식도 없다면, 상위 노드에게 삭제 가능 신호를 보냅니다.
            return !current.isEndOfWord && current.isEmpty();
        }

        return false;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        // 1. 입력받은 접두사가 끝나는 노드 위치를 찾습니다.
        TrieNode lastNode = getLastNode(prefix);
        if (lastNode != null) {
            // 2. 해당 지점부터 시작하여 깊이 우선 탐색(DFS)으로 모든 완성 가능한 단어를 수집합니다.
            findAllWords(lastNode, prefix, results);
        }
        return results;
    }

    private void findAllWords(TrieNode node, String currentWord, List<String> results) {
        // 1. 탐색 중 단어 완성 표시를 만날 때마다 결과 리스트에 해당 단어를 추가합니다.
        if (node.isEndOfWord) {
            results.add(currentWord);
        }
        // 2. 현재 노드와 연결된 모든 자식 노드들을 순회하며 단어의 조합을 이어갑니다.
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            findAllWords(entry.getValue(), currentWord + entry.getKey(), results);
        }
    }
}