package com.app.babybaby.service.purchase.purchase;

import com.app.babybaby.domain.boardDTO.eventDTO.EventDTO;
import com.app.babybaby.domain.calendarDTO.CalendarDTO;
import com.app.babybaby.domain.fileDTO.eventFileDTO.EventFileDTO;
import com.app.babybaby.domain.memberDTO.KidDTO;
import com.app.babybaby.domain.memberDTO.MemberDTO;
import com.app.babybaby.domain.purchaseDTO.couponDTO.CouponDTO;
import com.app.babybaby.domain.purchaseDTO.purchaseDTO.PurchaseDTO;
import com.app.babybaby.entity.board.event.Event;
import com.app.babybaby.entity.calendar.Calendar;
import com.app.babybaby.entity.file.eventFile.EventFile;
import com.app.babybaby.entity.member.Kid;
import com.app.babybaby.entity.purchase.coupon.Coupon;
import com.app.babybaby.entity.purchase.purchase.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface PurchaseService {
    //    나의 쿠폰 조회
    public Page<PurchaseDTO> findAllByMemberPaymentWithPage(Pageable pageable, Long memberId);

    public Page<PurchaseDTO> findAllByEventWithPage(Pageable pageable, Long memberId, LocalDateTime startDate);

    public PurchaseDTO findMemberIdByPaymentDetail(Long purchaseId);

    public EventDTO findAllInfoForPayment(Long memberId, Long eventId);

    public void processPayment(Long memberId, Long eventId, List<Kid> kids, String eventRegisterDate);

    public void saveAll(Long memberId, Long eventId, PurchaseDTO purchaseDTO);

    public Long findMemberByIdWithCount(Long memberId);

    default PurchaseDTO PurchaseToDTO(Purchase purchase){
        return PurchaseDTO.builder()
                .coupon(purchase.getCoupon())
                .eventTitle(purchase.getEvent().getBoardTitle())
                .id(purchase.getId())
                .memberId(purchase.getMember().getId())
                .purchaseCount(purchase.getPurchaseCount())
                .purchasePrice(purchase.getPurchasePrice())
                .purchaseRegisterDate(purchase.getPurchaseRegisterDate())
                .memberName(purchase.getMember().getMemberName())
                .eventFileDTOS(purchase.getEvent().getEventFiles().stream().map(eventFile -> eventFileToDTO(eventFile)).collect(Collectors.toList()))
                .memberPhone(purchase.getMember().getMemberPhone())
                .calendarDTOS(toCalendarDTO(purchase.getEvent().getCalendar()))
                .build();
    }

    default Purchase  dtoToPurchaseEntity(PurchaseDTO purchaseDTO){
        return Purchase.builder()
                .id(purchaseDTO.getId())
                .coupon(purchaseDTO.getCoupon())
                .purchaseCount(purchaseDTO.getPurchaseCount())
                .purchasePrice(purchaseDTO.getPurchasePrice())
                .purchaseRegisterDate(purchaseDTO.getPurchaseRegisterDate())
                .build();
    }

    default EventFileDTO eventFileToDTO(EventFile eventFile){
        return EventFileDTO.builder()
                .id(eventFile.getId())
                .fileOriginalName(eventFile.getFileOriginalName())
                .filePath(eventFile.getFilePath())
                .fileStatus(eventFile.getFileStatus())
                .fileUUID(eventFile.getFileUUID())
                .build();
    }

    default CalendarDTO toCalendarDTO(Calendar calendar){
        return CalendarDTO.builder()
                .id(calendar.getId())
                .calendarName(calendar.getCalendarName())
                .endDate(calendar.getEndDate())
                .startDate(calendar.getStartDate())
                .id(calendar.getId())
                .build();
    }

    default CouponDTO CouponToDTO(Coupon coupon){
        return CouponDTO.builder()
                .couponPrice(coupon.getCouponPrice())
                .couponStatus(coupon.getCouponStatus())
                .couponType(coupon.getCouponType())
                .id(coupon.getId())
                .memberId(coupon.getMember().getId())
                .registerDate(coupon.getRegisterDate())
                .updateDate(coupon.getUpdateDate())
                .build();
    }

    default Kid toKid(KidDTO kidDTO){
        return Kid.builder()
                .id(kidDTO.getId())
                .kidAge(kidDTO.getKidAge())
                .kidGender(kidDTO.getKidGender())
                .kidName(kidDTO.getKidName())
                .kidAge(kidDTO.getKidAge())
                .parent(kidDTO.getParent())
                .build();
    }

    default KidDTO toKidDTO(Kid kid){
        return KidDTO.builder()
                .id(kid.getId())
                .kidAge(kid.getKidAge())
                .kidGender(kid.getKidGender())
                .kidName(kid.getKidName())
                .kidAge(kid.getKidAge())
                .build();
    }

    default EventDTO eventToDTO(Event event){
        return EventDTO.builder()
                .id(event.getId())
                .boardContent(event.getBoardContent())
                .boardTitle(event.getBoardTitle())
                .category(event.getCategory())
                .eventLocation(event.getEventLocation())
                .eventPrice(event.getEventPrice())
                .eventRecruitCount(event.getEventRecruitCount())
                .memberId(event.getCompany().getId())
                .memberEmail(event.getCompany().getMemberEmail())
                .memberPhone(event.getCompany().getMemberPhone())
                .memberHiSentence(event.getCompany().getMemberHiSentence())
                .memberName(event.getCompany().getMemberName())
                .memberNickname(event.getCompany().getMemberNickname())
                .memberLocation(event.getCompany().getMemberAddress())
                .memberProfileOriginalName(event.getCompany().getMemberProfileOriginalName())
                .memberProfilePath(event.getCompany().getMemberProfilePath())
                .memberProfileUUID(event.getCompany().getMemberProfileUUID())
                .calendar(toCalendarDTO(event.getCalendar()))
                .files(event.getEventFiles().stream().map(eventFile -> eventFileToDTO(eventFile)).collect(Collectors.toList()))
                .build();
    }

}
