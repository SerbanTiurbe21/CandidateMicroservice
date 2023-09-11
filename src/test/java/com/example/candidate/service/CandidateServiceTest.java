package com.example.candidate.service;

import com.example.candidate.exception.DuplicateCandidateException;
import com.example.candidate.model.Candidate;
import com.example.candidate.repository.CandidateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.assertions.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository mockCandidateRepository;

    private CandidateServiceImpl candidateService;

    private static final String CANDIDATE_ID = "1";

    @Captor
    private ArgumentCaptor<Candidate> candidateCaptor;

    @BeforeEach
    void setUp() {
        candidateService = new CandidateServiceImpl(mockCandidateRepository);
    }

    @AfterEach
    void verifyInteractions() {
        Mockito.verifyNoMoreInteractions(mockCandidateRepository);
    }

    private static List<Candidate> makeCandidates() {

        var candidate1 = new Candidate();
        candidate1.setName("Zaza");
        candidate1.setPosition("Developer");
        candidate1.setPhoneNumber("222-1234");
        candidate1.setCvLink("link");

        var candidate2 = new Candidate();
        candidate2.setName("Baza");
        candidate2.setPosition("Junior Software Developer");
        candidate2.setPhoneNumber("222-1234");
        candidate2.setCvLink("test");

        var candidate3 = new Candidate();
        candidate3.setName("Zaza");
        candidate3.setPosition("Senior Developer");
        candidate3.setPhoneNumber("222-4321");
        candidate3.setCvLink("cvLink");

        var candidate4 = new Candidate();
        candidate4.setName("Lala");
        candidate4.setPosition("QA");
        candidate4.setPhoneNumber("222-1111");
        candidate4.setCvLink("cv");

        return Arrays.asList(candidate1, candidate2, candidate3, candidate4);
    }

    @Test
    public void givenNewCandidate_whenAddCandidate_thenCandidateAddedSuccessfully() {
        var expectedCandidate = new Candidate();
        expectedCandidate.setName("Andrei");
        expectedCandidate.setPosition("Junior Java");
        expectedCandidate.setPhoneNumber("0733742000");
        expectedCandidate.setCvLink("https://cvlink.com");
        expectedCandidate.setEmail("a.balan@theaccessgroup.com");

        when(mockCandidateRepository.save(expectedCandidate)).thenReturn(expectedCandidate);
        when(mockCandidateRepository.existsByPhoneNumber(expectedCandidate.getPhoneNumber())).thenReturn(false);
        when(mockCandidateRepository.existsByEmail(any())).thenReturn(false);

        var actualCandidate = candidateService.addCandidate(expectedCandidate);

        verify(mockCandidateRepository, times(1)).save(expectedCandidate);
        assertEquals(expectedCandidate, actualCandidate);
    }

    @Test
    public void givenNewCandidate_whenAddCandidate_thenUniqueCandidateByPhoneNumberAddedSuccessfully() {
        var existingCandidate = new Candidate();
        var duplicateCandidate = new Candidate();
        existingCandidate.setPhoneNumber("1234567890");
        duplicateCandidate.setPhoneNumber("1234567890");

        when(mockCandidateRepository.existsByPhoneNumber(duplicateCandidate.getPhoneNumber()))
                .thenThrow(DuplicateCandidateException.class);

        assertThrows(DuplicateCandidateException.class, () -> candidateService.addCandidate(duplicateCandidate));
    }

    @Test
    public void givenNewCandidate_whenAddCandidate_thenUniqueCandidateByEmailAddedSuccessfully() {
        var existingCandidate = new Candidate();
        var duplicateCandidate = new Candidate();
        existingCandidate.setEmail("test@example.com");
        duplicateCandidate.setEmail("test@example.com");

        when(mockCandidateRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(mockCandidateRepository.existsByEmail(duplicateCandidate.getEmail()))
                .thenThrow(DuplicateCandidateException.class);

        assertThrows(DuplicateCandidateException.class, () -> candidateService.addCandidate(duplicateCandidate));
    }

    @Test
    void givenCandidateId_whenGetCandidateById_thenCandidateReturned() {
        var expectedCandidate = new Candidate();
        expectedCandidate.setId(CANDIDATE_ID);
        expectedCandidate.setName("Bogdan");
        expectedCandidate.setPosition("Junior Software Developer");
        expectedCandidate.setPhoneNumber("555-1234");
        expectedCandidate.setCvLink("test");
        expectedCandidate.setEmail("bogdan@gmail.com");
        expectedCandidate.setInterviewDate(LocalDate.of(2015, Month.FEBRUARY, 11));
        when(mockCandidateRepository.findById(CANDIDATE_ID)).thenReturn(Optional.of(expectedCandidate));

        var actualCandidate = candidateService.getCandidateById(CANDIDATE_ID);

        assertEquals(expectedCandidate, actualCandidate.orElse(null));
    }

    @Test
    void givenNonExistentCandidateId_whenGetCandidateById_thenEmptyOptionalReturned() {
        when(mockCandidateRepository.findById(CANDIDATE_ID)).thenReturn(Optional.empty());

        Optional<Candidate> result = candidateService.getCandidateById(CANDIDATE_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenPositionWhenRetrievingCandidatesThenReturnCandidatesWhichAppliedForTheSpecifiedPosition() {

        String position = "test";
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        ;
        List<Candidate> expectedCandidates = makeCandidates();

        when(mockCandidateRepository.findByPosition(sort, position)).thenReturn(expectedCandidates);

        var actualCandidates = candidateService.getCandidateByPosition(position);

        assertEquals(expectedCandidates, actualCandidates);
    }

    @Test
    public void givenExistentCandidate_whenUpdateExistentCandidate_thenCandidateUpdatedSuccessfully() {
        String candidateId = "123";
        var expectedCandidate = new Candidate();
        expectedCandidate.setId(candidateId);
        expectedCandidate.setName("John Doe");
        expectedCandidate.setPosition("Software Developer");
        expectedCandidate.setPhoneNumber("1234567890");
        expectedCandidate.setEmail("john.doe@example.com");
        expectedCandidate.setCvLink("https://example.com/cv");
        expectedCandidate.setInterviewDate(LocalDate.of(2015, Month.FEBRUARY, 11));

        var updatedCandidate = new Candidate();
        updatedCandidate.setName("Jane Smith");
        updatedCandidate.setPosition("Senior Developer");
        updatedCandidate.setPhoneNumber("9876543210");
        updatedCandidate.setEmail("jane.smith@example.com");
        updatedCandidate.setCvLink("https://example.com/cvlink");
        updatedCandidate.setInterviewDate(LocalDate.of(2019, Month.MARCH, 18));

        when(mockCandidateRepository.findById(candidateId)).thenReturn(Optional.of(expectedCandidate));
        when(mockCandidateRepository.save(any())).thenReturn(expectedCandidate);

        candidateService.updateCandidate(candidateId, updatedCandidate);

        assertEquals(updatedCandidate.getName(), expectedCandidate.getName());
        assertEquals(updatedCandidate.getPosition(), expectedCandidate.getPosition());
        assertEquals(updatedCandidate.getPhoneNumber(), expectedCandidate.getPhoneNumber());
        assertEquals(updatedCandidate.getEmail(), expectedCandidate.getEmail());
        assertEquals(updatedCandidate.getCvLink(), expectedCandidate.getCvLink());
        assertEquals(updatedCandidate.getInterviewDate(), expectedCandidate.getInterviewDate());

        verify(mockCandidateRepository).save(candidateCaptor.capture());
        var actualCandidate = candidateCaptor.getValue();
        assertEquals(expectedCandidate, actualCandidate);
    }

    @Test
    void givenNonExistentCandidateName_whenGetCandidateByName_thenEmptyIsReturned() {

        String name = "abc";
        when(mockCandidateRepository.findByNameIgnoreCase(name)).thenReturn(Collections.emptyList());

        var actualCandidateList = candidateService.getCandidateByName(name);

        assertEquals(0, actualCandidateList.size());
    }


    @Test
    void givenCandidateName_whenGetCandidateByName_thenReturnCandidateWithThatSpecifiedName() {
        var expectedCandidate = List.of(new Candidate("1",
                "Alexandra",
                "QA",
                "0123456789",
                "cv",
                "ale@email.com",
                null,
                "random"));

        when(mockCandidateRepository.findByNameIgnoreCase(expectedCandidate.get(0).getName()))
                .thenReturn(expectedCandidate);

        var actualCandidate = candidateService.getCandidateByName(expectedCandidate.get(0).getName());

        assertEquals(expectedCandidate, actualCandidate);
    }

    @Test
    void givenCandidateName_whenGetCandidateByName_thenReturnMultipleCandidatesWithThatSpecifiedName() {
        String name = "Zaza";
        List<Candidate> expectedCandidates = makeCandidates();

        var filteredCandidates = expectedCandidates
                .stream()
                .filter(candidate -> candidate.getName().equals(name))
                .collect(Collectors.toList());

        when(mockCandidateRepository.findByNameIgnoreCase(name)).thenReturn(filteredCandidates);

        var actualCandidates = candidateService.getCandidateByName(name);

        assertEquals(filteredCandidates, actualCandidates);
    }
}
