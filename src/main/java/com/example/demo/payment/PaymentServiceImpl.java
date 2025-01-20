package com.example.demo.payment;

import com.example.demo.db.*;
import com.example.demo.db.repo.CustomerRepo;
import com.example.demo.db.repo.LoanInstallmentRepo;
import com.example.demo.db.repo.LoanRepo;
import com.example.demo.payment.dto.PaymentRequestDTO;
import com.example.demo.payment.dto.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.temporal.ChronoUnit;

@Service
public class PaymentServiceImpl implements PaymentService {

    static final Double BONUS = 0.001;
    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private LoanInstallmentRepo installmentRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public PaymentResponseDTO payLoan(PaymentRequestDTO requestDto) {
        LocalDate paymentDate = requestDto.getPaymentDate();
        Long loanId = requestDto.getLoanId();
        Double amount = requestDto.getAmount();

        List<LoanInstallment> installments =
                installmentRepo.findByLoanIdAndIsPaidOrderByDueDateAsc(loanId, false);

        List<LoanInstallment> paidInstallments = new ArrayList<>();

        Optional<Loan> loanOpt = loanRepo.findById(loanId);
        Loan loan = loanOpt.orElseThrow();

        int indexOfInstallment = 0;
        double totalPaidAmount = 0.0;
        Double paidLimit = 0.0;
        while (amount > 0.0 && indexOfInstallment < installments.size()) {
            LoanInstallment installment = installments.get(indexOfInstallment);
            double newAmountOfInstallment = calculatePaymentAmount(paymentDate, installment);

            if (newAmountOfInstallment <= 0.0) {
                break;
                //No valid installment exists
            }
            if (newAmountOfInstallment <= amount) {

                //update installment object
                installment.setPaidAmount(newAmountOfInstallment);
                installment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                installment.setIsPaid(true);

                paidInstallments.add(installment);

                amount -= newAmountOfInstallment;
                totalPaidAmount += newAmountOfInstallment;
                paidLimit += installment.getAmount();
                indexOfInstallment++;
            }
            else {
                break;
                //not enough amount
            }
        }

        //all installments are paid
        if (paidInstallments.size() == installments.size()) {
            loan.setIsPaid(true); //close loan
            loanRepo.save(loan);
        }
        /*
            Necessary updates should be done in customer, loan and installment tables.
         */
        if (paidLimit > 0.0) {
            installmentRepo.saveAll(paidInstallments);

            //update user used credit limit
            Customer customer = loan.getCustomer();
            customer.setUsedCreditLimit(customer.getUsedCreditLimit() - paidLimit);
            customerRepo.save(customer);
        }

        /*
        A result should be returned to inform how many installments paid, total
        amount spent and if loan is paid completely.
         */
        PaymentResponseDTO responseDto = new PaymentResponseDTO();
        responseDto.setLoanId(loanId);
        responseDto.setCountOfPaidInstallments(paidInstallments.size());
        responseDto.setTotalSpendingAmount(totalPaidAmount);
        responseDto.setIsLoanClosed(loan.getIsPaid());

        return responseDto;
    }

    private Double calculatePaymentAmount(LocalDate paymentDate, final LoanInstallment installment) {

        Timestamp stamp = installment.getDueDate();
        LocalDate dueDate = stamp.toLocalDateTime().toLocalDate();

        /*
        Installments have due date that still more than 3 calendar months cannot be
        paid. So if we were in January, you could pay only for January, February and
        March installments.
         */
        long monthsBetween = ChronoUnit.MONTHS.between(paymentDate.withDayOfMonth(1), dueDate);
        if (monthsBetween > 2) {
            return 0.0; //no avaliable installment to pay
        }

        /*
        Bonus 2: For reward and penalty add this logic to paying loan flow:
        - If an installment is paid before due date, make a discount equal to
        0.001*(number of days before due date) So in this case paidAmount of
        installment will be lower than amount.
        - If an installment is paid after due date, add a penalty equal to 0.001*(number
        of days after due date) So in this case paidAmount of installment will be
        higher than amount.
         */
        //calculate discount or punishment
        long daysBetween = ChronoUnit.DAYS.between(paymentDate, dueDate);
        //positive value means penalty, negative value discount
        Double paymentDifference = daysBetween * BONUS;
        return installment.getAmount() - paymentDifference;
    }
}
