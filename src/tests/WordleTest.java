package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.RandomWordGenerator;
import model.Wordle;
class WordleTest {
    
    private Wordle wordle;
    
    @BeforeEach
    void setUp() throws Exception {
        wordle = new Wordle("hello");
    }
    @Test
    void testCheckWord_correct() {
        boolean result = wordle.checkWord("hello");
        assertTrue(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {2, 2, 2, 2, 2}, checks);
    }
    
    @Test
    void testCheckWord_repeatLetter() {
        boolean result = wordle.checkWord("ladle");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {1, 0, 0, 2, 1}, checks);
    }
        
    
    @Test
    void testCheckWord_partialMatch() {
        boolean result = wordle.checkWord("hills");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {2, 0, 2, 2, 0}, checks);
    }
    
    @Test
    void testCheckWord_correct2() {
    	Wordle thisTest = new Wordle("bates");
        boolean result = thisTest.checkWord("sheet");
        assertFalse(result);
        int[] checks = thisTest.getChecks();
        
        assertArrayEquals(new int[] {1, 0, 0, 2, 1}, checks);
        
    }
    
    @Test
    void testCheckWord_correct3() {
    	Wordle thisTest = new Wordle("wheen");
        boolean result = thisTest.checkWord("heete");
        assertFalse(result);
        int[] checks = thisTest.getChecks();
        assertArrayEquals(new int[] {1, 1, 2, 0, 0}, checks);

    }

    @Test
    void testCheckWord_emptyString() {
        boolean result = wordle.checkWord("");
        assertFalse(result);
    }

    @Test
    void testCheckWord_null() {
        assertThrows(NullPointerException.class, () -> wordle.checkWord(null));
    }

    @Test
    void testCheckWord_singleLetter() {
        boolean result = wordle.checkWord("h");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_tooLong() {
        boolean result = wordle.checkWord("helloworld");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_missingLetter() {
        boolean result = wordle.checkWord("help");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_repeatedLetters() {
        boolean result = wordle.checkWord("hell");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_allLettersUsed() {
        boolean result = wordle.checkWord("helloo");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_allLettersUsedOnce() {
        boolean result = wordle.checkWord("lohe");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_allLettersUsedTwice() {
        boolean result = wordle.checkWord("helloh");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {0, 0, 0, 0, 0}, checks);
    }

    @Test
    void testCheckWord_caseInsensitive() {
        boolean result = wordle.checkWord("HELLO");
        assertTrue(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {2, 2, 2, 2, 2}, checks);
    }

    @Test
    void testCheckWord_specialCharacters() {
        boolean result = wordle.checkWord("h!llo");
        assertFalse(result);
        int[] checks = wordle.getChecks();
        assertArrayEquals(new int[] {2, 0, 2, 2, 2}, checks);
    }
    @Test 
    void getRandomWord() throws FileNotFoundException {
    	String word = RandomWordGenerator.getWord();
    	ArrayList<String> dict = RandomWordGenerator.getDictionary();
    }


}