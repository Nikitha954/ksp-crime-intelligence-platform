package com.example.demo.controller;

import com.example.demo.entity.Accused;
import com.example.demo.entity.CaseMaster;
import com.example.demo.repository.AccusedRepository;
import com.example.demo.repository.CaseMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    @Autowired private AccusedRepository accusedRepo;
    @Autowired private CaseMasterRepository caseRepo;

    @GetMapping("/{accusedMasterId}")
    public ResponseEntity<Map<String, Object>> getNetworkGraph(@PathVariable Long accusedMasterId) {
        Optional<Accused> accusedOpt = accusedRepo.findById(accusedMasterId);
        if (accusedOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Accused primaryAccused = accusedOpt.get();
        String personID = primaryAccused.getPersonID();

        // 1. Find all accused records matching this PersonID
        List<Accused> allInstancesOfPerson = (personID != null && !personID.startsWith("A")) ?
                accusedRepo.findByPersonID(personID) : Collections.singletonList(primaryAccused);

        List<Long> linkedCaseIds = new ArrayList<>();
        for (Accused a : allInstancesOfPerson) {
            if (!linkedCaseIds.contains(a.getCaseMasterID())) {
                linkedCaseIds.add(a.getCaseMasterID());
            }
        }

        List<CaseMaster> linkedCases = caseRepo.findByCaseMasterIDIn(linkedCaseIds);
        List<Accused> coAccusedList = accusedRepo.findByCaseMasterIDIn(linkedCaseIds);

        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();
        Set<String> addedNodeIds = new HashSet<>();

        // Add primary accused node
        String targetAccusedNodeId = "accused-" + primaryAccused.getAccusedMasterID();
        nodes.add(createNode(targetAccusedNodeId, primaryAccused.getAccusedName() + " (" + (personID != null ? personID : "ID:" + accusedMasterId) + ")", "accused", 15));
        addedNodeIds.add(targetAccusedNodeId);

        // Add linked cases and police station locations
        for (CaseMaster cm : linkedCases) {
            String caseNodeId = "case-" + cm.getCaseMasterID();
            if (!addedNodeIds.contains(caseNodeId)) {
                nodes.add(createNode(caseNodeId, "FIR: " + cm.getCrimeNo() + " [" + cm.getCrimeSubHeadName() + "]", "case", 10));
                addedNodeIds.add(caseNodeId);
            }
            edges.add(createEdge(targetAccusedNodeId, caseNodeId, "IMPLICATED_IN"));

            // Police station location node
            String psNodeId = "ps-" + cm.getPoliceStationID();
            if (!addedNodeIds.contains(psNodeId)) {
                nodes.add(createNode(psNodeId, cm.getPoliceStationName() + " (" + cm.getDistrictName() + ")", "location", 8));
                addedNodeIds.add(psNodeId);
            }
            edges.add(createEdge(caseNodeId, psNodeId, "REGISTERED_AT"));
        }

        // Add co-accused nodes
        for (Accused co : coAccusedList) {
            if (co.getAccusedMasterID().equals(primaryAccused.getAccusedMasterID())) continue;

            String coNodeId = "accused-" + co.getAccusedMasterID();
            if (!addedNodeIds.contains(coNodeId)) {
                nodes.add(createNode(coNodeId, co.getAccusedName() + " (" + (co.getPersonID() != null ? co.getPersonID() : "A") + ")", "co-accused", 10));
                addedNodeIds.add(coNodeId);
            }

            String caseNodeId = "case-" + co.getCaseMasterID();
            edges.add(createEdge(coNodeId, caseNodeId, "CO_ACCUSED_IN"));
        }

        Map<String, Object> graph = new HashMap<>();
        graph.put("accusedMasterId", accusedMasterId);
        graph.put("personID", personID);
        graph.put("accusedName", primaryAccused.getAccusedName());
        graph.put("totalLinkedCases", linkedCases.size());
        graph.put("nodes", nodes);
        graph.put("edges", edges);

        return ResponseEntity.ok(graph);
    }

    @GetMapping("/offenders")
    public ResponseEntity<List<Map<String, Object>>> getTopOffenders() {
        List<Accused> all = accusedRepo.findAll();
        Map<String, Integer> personCounts = new HashMap<>();
        Map<String, Accused> personSample = new HashMap<>();

        for (Accused a : all) {
            if (a.getPersonID() != null && a.getPersonID().startsWith("KA-ACC-")) {
                personCounts.put(a.getPersonID(), personCounts.getOrDefault(a.getPersonID(), 0) + 1);
                personSample.putIfAbsent(a.getPersonID(), a);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : personCounts.entrySet()) {
            Accused sample = personSample.get(entry.getKey());
            Map<String, Object> item = new HashMap<>();
            item.put("accusedMasterId", sample.getAccusedMasterID());
            item.put("personID", entry.getKey());
            item.put("accusedName", sample.getAccusedName());
            item.put("caseCount", entry.getValue());
            result.add(item);
        }

        result.sort((a, b) -> Integer.compare((Integer) b.get("caseCount"), (Integer) a.get("caseCount")));

        return ResponseEntity.ok(result);
    }

    private Map<String, Object> createNode(String id, String label, String type, int val) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", id);
        node.put("label", label);
        node.put("type", type);
        node.put("value", val);
        return node;
    }

    private Map<String, Object> createEdge(String source, String target, String type) {
        Map<String, Object> edge = new HashMap<>();
        edge.put("source", source);
        edge.put("target", target);
        edge.put("type", type);
        return edge;
    }
}
