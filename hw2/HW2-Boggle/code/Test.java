public class Test {

    public static void main(String[] args) {
        DictionaryTrie dt = new DictionaryTrie("BoggleDictionary.txt");
        System.out.println(dt.size());
        dt.add("amsmdmf");
        System.out.println(dt.size());
        System.out.println(dt.inDictionary("amsmdm"));
    }
}