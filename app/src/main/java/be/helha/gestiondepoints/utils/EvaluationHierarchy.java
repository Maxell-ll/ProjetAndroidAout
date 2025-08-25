package be.helha.gestiondepoints.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.helha.gestiondepoints.models.Evaluation;

//pemret de gérer la hiérarchie des évaluations (indente les évaluations dans le fragment en fonction de leur parent)
public class EvaluationHierarchy {

    public static Map<Long, List<Evaluation>> buildHierarchy(List<Evaluation> evaluations) {
        //association entre l'id du parent et la liste des enfants (clef = id du parent, valeur = liste des enfants)
        Map<Long, List<Evaluation>> hierarchy = new HashMap<>();

        for (Evaluation evaluation : evaluations) {
            long parentId = evaluation.getParentEvaluationId();
            if (!hierarchy.containsKey(parentId)) {
                hierarchy.put(parentId, new ArrayList<>());
            }
            hierarchy.get(parentId).add(evaluation);
        }
        return hierarchy;
    }

    public static List<Evaluation> flattenHierarchy(Map<Long, List<Evaluation>> hierarchy, Long parentId) {
        List<Evaluation> flatList = new ArrayList<>();
        flattenHierarchyHelper(hierarchy, parentId, flatList);
        return flatList;
    }

    private static void flattenHierarchyHelper(Map<Long, List<Evaluation>> hierarchy, Long parentId, List<Evaluation> flatList) {
        if (hierarchy.containsKey(parentId)) {
            for (Evaluation evaluation : hierarchy.get(parentId)) {
                flatList.add(evaluation);
                flattenHierarchyHelper(hierarchy, evaluation.getId(), flatList);
            }
        }
    }

}
