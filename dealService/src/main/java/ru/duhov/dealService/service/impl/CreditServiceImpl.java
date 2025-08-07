package ru.duhov.dealService.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.duhov.dealService.dto.CreditDto;
import ru.duhov.dealService.dto.enums.CreditStatus;
import ru.duhov.dealService.entity.Credit;
import ru.duhov.dealService.repository.CreditRepository;
import ru.duhov.dealService.service.CreditService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit createCredit(CreditDto creditDto) {
        log.info("Creating new credit with data: {}", creditDto);
        Credit credit = Credit.builder()
                .creditStatus(CreditStatus.CALCULATED)
                .amount(creditDto.getAmount())
                .psk(creditDto.getPsk())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .salaryClient(creditDto.getIsSalaryClient())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .paymentSchedule(creditDto.getPaymentSchedule().toString())
                .rate(creditDto.getRate())
                .term(creditDto.getTerm())
                .build();

        credit = creditRepository.save(credit);
        log.info("Credit created: {}", credit);
        return credit;
    }
}
