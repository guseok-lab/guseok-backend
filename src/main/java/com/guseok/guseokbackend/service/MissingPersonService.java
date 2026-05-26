package com.guseok.guseokbackend.service;

import com.guseok.guseokbackend.common.exception.BusinessException;
import com.guseok.guseokbackend.common.exception.ErrorCode;
import com.guseok.guseokbackend.dto.file.ImageUploadUrlResponse;
import com.guseok.guseokbackend.dto.missingperson.MissingPersonCreateRequest;
import com.guseok.guseokbackend.dto.missingperson.MissingPersonResponse;
import com.guseok.guseokbackend.entity.Member;
import com.guseok.guseokbackend.entity.MissingPerson;
import com.guseok.guseokbackend.entity.MissingStatus;
import com.guseok.guseokbackend.repository.MemberRepository;
import com.guseok.guseokbackend.repository.MissingPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissingPersonService {

    private final MissingPersonRepository missingPersonRepository;
    private final MemberRepository memberRepository;
    private final OciStorageService ociStorageService;

    public ImageUploadUrlResponse generateImageUploadUrl(String originalFilename) {
        String ext = extractExtension(originalFilename);
        String objectKey = "missing-person-images/" + UUID.randomUUID() + "." + ext;
        String uploadUrl = ociStorageService.generatePresignedPutUrl(objectKey);
        return new ImageUploadUrlResponse(objectKey, uploadUrl);
    }

    @Transactional
    public MissingPersonResponse create(Long memberId, MissingPersonCreateRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String photoUrl = StringUtils.hasText(request.photoObjectKey())
            ? ociStorageService.buildObjectUrl(request.photoObjectKey())
            : null;

        MissingPerson missingPerson = MissingPerson.builder()
            .member(member)
            .name(request.name())
            .age(request.age())
            .gender(request.gender())
            .height(request.height())
            .weight(request.weight())
            .appearanceDescription(request.appearanceDescription())
            .bodyType(request.bodyType())
            .lastLocation(request.lastLocation())
            .missingCircumstance(request.missingCircumstance())
            .contact(request.contact())
            .photoUrl(photoUrl)
            .build();

        return MissingPersonResponse.from(missingPersonRepository.save(missingPerson));
    }

    @Transactional(readOnly = true)
    public List<MissingPersonResponse> getMyMissingPersons(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return missingPersonRepository.findByMember(member)
            .stream()
            .map(MissingPersonResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public long countSearchingMissingPersons() {
        return missingPersonRepository.countByStatus(MissingStatus.SEARCHING);
    }

    @Transactional(readOnly = true)
    public List<MissingPersonResponse> getSearchingMissingPersons() {
        return missingPersonRepository.findByStatus(MissingStatus.SEARCHING)
            .stream()
            .map(MissingPersonResponse::from)
            .toList();
    }

    @Transactional
    public void delete(Long memberId, Long missingPersonId) {
        MissingPerson missingPerson = missingPersonRepository.findById(missingPersonId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MISSING_PERSON_NOT_FOUND));

        if (!missingPerson.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.MISSING_PERSON_ACCESS_DENIED);
        }

        missingPersonRepository.delete(missingPerson);
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}