package com.optify.utils;

import com.optify.exceptions.DataException;
import info.debatty.java.stringsimilarity.Levenshtein;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ComparisonUtils {
    private static final Set<String> TYPE_KEYWORDS = new HashSet<>(Arrays.asList(
            "harina","fideo","aceite","arroz","pan","gelatina","light", "maiz", "girasol",
            "oliva","integral","parboiled"
    ));
    private static final Levenshtein lev = new Levenshtein();
    private static Logger logger = LoggerFactory.getLogger(ComparisonUtils.class);
    //Reparar errores de encoding (Mojibake)
    public static String repairEncoding(String text) {
        if(text == null) return null;
        if(text.contains("Ã") || text.contains("Â")) {
            return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
        return text;
    }

    public static String normalize(String text) {
        if(text == null) return "";

        String temp = text.toLowerCase();
        temp = deleteDiacriticalMarks(temp);
        temp = separateNumbersFromLetters(temp);
        temp = normalizeUnits(temp);
        temp = normalizeSynonyms(temp);
        temp = cleanMarks(temp);
        temp = removeStopWords(temp);
        temp = collapseGaps(temp);
        return temp;
    }

    public static String deleteDiacriticalMarks(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return text;
    }

    public static String separateNumbersFromLetters(String text) {
        text = text.replaceAll("(\\d+)([a-zA-Z]+)", "$1 $2");
        text = text.replaceAll("([a-zA-Z]+)(\\d+)", "$1 $2");
        return text;
    }

    public static String cleanMarks(String text) {
        text = text.replaceAll("[\\.\\,\\(\\)\\-\\_\\/]", " ");
        text = text.replaceAll("[^a-z0-9\\s]", "");
        return text;
    }

    public static String collapseGaps(String text) {
        text = text.replaceAll("\\s+", " ");
        return text.trim();
    }

    public static boolean compare(String name1, String name2) throws DataException {
        if(name1 == null || name2 == null) {
            throw new DataException("Los nombres no pueden ser nulos. { Name1: " + name1 + "; Name2: " + name2 + "}");
        }

        String nameRepared1 = repairEncoding(normalize(name1));
        String nameRepared2 = repairEncoding(normalize(name2));

        if(!haveSameQuantity(nameRepared1,nameRepared2)) {
            logger.info("[CANTIDAD DIFERENTE] {} vs {} -> Rechazado", name1, name2);
            return false;
        }

        if(!haveSameCategoryKeywords(nameRepared1,nameRepared2)) {
            logger.info("[CATEGORIA DIFERENTE] {} vs {} -> Rechazado", name1, name2);
            return false;
        }
        double similarity = 0;
        similarity = calculateJaccardByWords(nameRepared1,nameRepared2);
        if (similarity > 90) {
            logger.debug(String.format("*[RESULTADO x JACCARD]*name1=%s*name2=%s*similarity=%f*resultado=¡Match!", name1, name2,similarity));
            return true;
        }else {
            logger.debug(String.format("*[RESULTADO x JACCARD]*name1=%s*name2=%s*similarity=%f*resultado=¡NO Match!", name1, name2, similarity));
            similarity = calculateHybridSimilarity(nameRepared1, nameRepared2);
            if (similarity > 80) {
                logger.debug(String.format("*[RESULTADO HIBRIDO]*name1=%s*name2=%s*similarity=%f*resultado=¡Match!", name1, name2, similarity));
                return true;
            } else {
                logger.debug(String.format("*[RESULTADO ENCODING]*name1=%s*name2=%s*similarity=%f*resultado=¡NO Match!", name1, name2, similarity));
                return false;
            }
        }
    }

    private static boolean haveSameCategoryKeywords(String normalizedName1, String normalizedName2) {
        for(String keyword : TYPE_KEYWORDS) {
            //Si un nombre tiene una keyword y el otro no, no son el mismo producto.
            if(normalizedName1.contains(keyword) != normalizedName2.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    private static boolean haveSameQuantity(String name1, String name2) {
        String q1 = extractQuantity(name1);
        String q2 = extractQuantity(name2);
        if(q1 != null && q2 != null) {
            return q1.equals(q2);
        }
        //Si no tenemos info. para comparar, permitimos continuar.
        return true;
    }

    private static String extractQuantity(String text) {
        if(text == null || text.isEmpty()) return null;

        Pattern pattern = Pattern.compile("(\\d+[.,]?\\d*)\\s*(g|ml|l|k)\\b");
        Matcher matcher = pattern.matcher(text);

        if(matcher.find()) {
            return matcher.group(1).replace(",",".") + matcher.group(2);
        }
        return null;
    }


    private static double calculateHybridSimilarity(String name1, String name2) {
        String[] words1 = name1.split("\\s+");
        String[] words2 = name2.split("\\s+");

        Set<String> set1 = new HashSet<>(Arrays.asList(words1));
        Set<String> set2 = new HashSet<>(Arrays.asList(words2));

        int coincidences = 0;
        Set<String> alredyUsedFromSet2 = new HashSet<>();
        for(String w1 : set1) {
            for(String w2 : set2) {
                if(alredyUsedFromSet2.contains(w2)) continue;

                if(w1.equals(w2) || isTooSimilar(w1,w2)) {
                    coincidences++;
                    alredyUsedFromSet2.add(w2);
                    break;
                }
            }
        }
        int unionSize =set1.size()+ set2.size() - coincidences;
        return (double) coincidences / unionSize * 100;
    }

    private static boolean isTooSimilar(String w1, String w2) {
        if(w1.matches(".*\\d.*") || w2.matches(".*\\d.*")) return w1.equals(w2);
        if(w1.length() <= 3) return w1.equals(w2);
        double distance = lev.distance(w1,w2);
        return distance <= 1;
    }


    private static double calculateJaccardByWords(String name1, String name2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(name1.split("\\s+")));
        Set<String> set2 = new HashSet<>(Arrays.asList(name2.split("\\s+")));

        //Calcular interseccion (palabras que estan en ambos sets)
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        //Calcular unión (palabras únicas combinadas)
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        //Calcular Jaccard: Interseccion / union
        if(union.isEmpty()) return 0;
        return (double) intersection.size() / union.size() * 100;
    }

    private static String normalizeUnits(String text) {
        if (text == null) return null;
        // Unificar gramos
        text = text.replaceAll("\\b(gr|grs|gramos|grms)\\b", "g");
        // Unificar mililitros / litros
        text = text.replaceAll("\\b(ml|mls|c\\.c\\.|cc)\\b", "ml");
        text = text.replaceAll("\\b(lts|litros|lt|l)\\b", "l");
        // Unificar kilogramos
        text = text.replaceAll("\\b(kg|kilos|kilogramos|kgs)\\b","k");
        // Unificar porcentajes (importante para el 0% que vimos en el log)
        //text = text.replaceAll("%", " porciento");
        return text;
    }

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "de", "con", "el", "la", "un", "una", "para", "por", "al", "en", "del"
    ));

    private static String removeStopWords(String text) {
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.joining(" "));
    }

    private static String normalizeSynonyms(String text) {
        // Caso específico del mercado uruguayo
        return text.replaceAll("\\b(dietetico|diet)\\b", "light");
    }
}
