package posts.tree;

import java.util.List;

public class TrieTest {
    public static void main(String[] args) {
        MyTrie trie = new MyTrie();

        // 1. 단어 삽입 테스트
        System.out.println("=== [1] 단어 삽입 ===");
        String[] words = {"apple", "app", "apricot", "ball", "bat", "battle"};
        for (String word : words) {
            System.out.println("삽입: " + word);
            trie.insert(word);
        }

        // 2. 검색 테스트 (정확한 단어 일치)
        System.out.println("\n=== [2] 단어 검색 테스트 ===");
        System.out.println("search('apple'): " + trie.search("apple"));   // true
        System.out.println("search('app'): " + trie.search("app"));       // true
        System.out.println("search('apri'): " + trie.search("apri"));     // false (접두사일 뿐 단어는 아님)
        System.out.println("search('ball'): " + trie.search("ball"));     // true
        System.out.println("search('cat'): " + trie.search("cat"));       // false

        // 3. 접두사 검색 테스트 (startsWith)
        System.out.println("\n=== [3] 접두사 검색 테스트 (startsWith) ===");
        System.out.println("startsWith('ap'): " + trie.startsWith("ap")); // true
        System.out.println("startsWith('bat'): " + trie.startsWith("bat")); // true
        System.out.println("startsWith('ca'): " + trie.startsWith("ca")); // false

        // 4. 자동 완성 테스트
        System.out.println("\n=== [4] 자동 완성 (Prefix: 'ba') ===");
        List<String> suggestions = trie.autocomplete("ba");
        System.out.println("'ba'로 시작하는 단어들: " + suggestions); // [ball, bat, battle]

        System.out.println("\n=== [4-1] 자동 완성 (Prefix: 'ap') ===");
        List<String> suggestions2 = trie.autocomplete("ap");
        System.out.println("'ap'로 시작하는 단어들: " + suggestions2); // [app, apple, apricot]

        // 5. 삭제 테스트
        System.out.println("\n=== [5] 단어 삭제 테스트 ===");
        System.out.println("'apple' 삭제 수행");
        trie.delete("apple");

        System.out.println("삭제 후 search('apple'): " + trie.search("apple")); // false
        System.out.println("삭제 후 search('app'): " + trie.search("app"));     // true (공통 접두사는 유지되어야 함)
        System.out.println("삭제 후 'ap' 자동 완성: " + trie.autocomplete("ap")); // [app, apricot]
    }
}
