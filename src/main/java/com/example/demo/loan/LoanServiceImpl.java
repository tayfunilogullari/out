package com.example.demo.loan;

import com.example.demo.ErrorType;
import com.example.demo.db.*;
import com.example.demo.db.repo.CustomerRepo;
import com.example.demo.db.repo.LoanInstallmentRepo;
import com.example.demo.db.repo.LoanRepo;
import com.example.demo.loan.dto.LoanDTO;
import com.example.demo.loan.dto.LoanInstallmentDTO;
import com.example.demo.loan.dto.LoanRequestDTO;
import com.example.demo.loan.dto.LoanResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {

    static final Set<Integer> INSTALLMENT_NUMBERS = Set.of(6, 9, 12, 24);
    static final Double INTEREST_RATE_MIN_VAL = 0.1;
    static final Double INTEREST_RATE_MAX_VAL = 0.5;

    @Autowired
    private LoanRepo loanRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LoanInstallmentRepo installmentRepo;
    @Autowired
    private CustomerRepo customerRepo;


    @Override
    public LoanDTO getById(Long id) {

        Optional<Loan> loanOpt = loanRepo.findById(id);
        Loan loan = loanOpt.orElseThrow();
        LoanDTO dto = modelMapper.map(loan, LoanDTO.class);

        return dto;
    }

    @Override
    public List<LoanDTO> getByCustomer(Long id) {
        Collection<Loan> loans = loanRepo.findByCustomerId(id);
        List<LoanDTO> dtoList = loans.stream()
                                .map(ld -> modelMapper.map(ld,LoanDTO.class))
                                .toList();
        return dtoList;
    }

    @Override
    public Boolean makePaid(Long id) {
        try {
            Optional<Loan> loanOpt = loanRepo.findById(id);
            Loan loan = loanOpt.orElseThrow();
            loan.setIsPaid(true);
            loanRepo.save(loan);
        }
        catch (Exception ex) {
            return false;
        }

        return true;
    }

    /*
    Create a new loan for a given customer, amount, interest rate and
    number of installments
     */
    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO requestDto) {
        LoanResponseDTO responseDto = new LoanResponseDTO();
        responseDto.setRequestDTO(requestDto);

        Optional<Customer> customerOPT = customerRepo.findById(requestDto.getCustomerId());
        Customer customer = customerOPT.orElseThrow();

        //find total amount with interest
        final Double totalAmount = calculateTotalAmount(requestDto);

        //You should check if customer has enough limit to get this new loan
        if (!checkCustomerCreditLimit(customer.getCreditLimit(),customer.getUsedCreditLimit(),totalAmount)){
            responseDto.setErrorType(ErrorType.NOT_ENOUGH_LIMIT.ordinal());
            responseDto.setIsAccepted(false);
            return responseDto;
        }

        //check for number of installments
        //Number of installments can only be 6, 9, 12, 24
        if (!INSTALLMENT_NUMBERS.contains(requestDto.getNumberOfInstallment())){
            responseDto.setErrorType(ErrorType.INSTALLMENT_NUMBER_FAULT.ordinal());
            responseDto.setIsAccepted(false);
            return responseDto;
        }

        //check for rate of interest
        //Interest rate can be between 0.1 – 0.5
        if (requestDto.getInterestRate() > INTEREST_RATE_MAX_VAL && requestDto.getInterestRate() < INTEREST_RATE_MIN_VAL) {
            responseDto.setErrorType(ErrorType.INTEREST_RATE_ERROR.ordinal());
            responseDto.setIsAccepted(false);
            return responseDto;
        }

        Loan loan = createLoanEntity(requestDto);
        loan.setCustomer(customer);
        loan = loanRepo.save(loan);

        Double newLimit = customer.getUsedCreditLimit() + totalAmount;
        customer.setUsedCreditLimit(newLimit);
        customerRepo.save(customer);

        final Double oneInstallmentAmount = totalAmount /  requestDto.getNumberOfInstallment();
        createLoanInstallments(loan, requestDto, oneInstallmentAmount);

        responseDto.setIsAccepted(true);
        responseDto.setErrorType(ErrorType.SUCCESS.ordinal());
        responseDto.setAmountOfOneInstallment(oneInstallmentAmount);

        return responseDto;
    }

    /*
    List Loans: List loans for a given customer

    If you want you can add more filters like number of installments, is paid etc
     */
    @Override
    public List<LoanDTO> getLoanList(LoanRequestDTO requestDto) {
        //TODO: EntityManger createQuery()

        Long userId = requestDto.getCustomerId();
        Collection<Loan> loans = loanRepo.findByCustomerId(userId);
        List<LoanDTO> dtoList =new ArrayList<>();

        loans.forEach(loan -> {
            dtoList.add(modelMapper.map(loan,LoanDTO.class));
        });

        return dtoList;
    }

    /*
    List Installment: List installments for a given loan
     */
    @Override
    public List<LoanInstallmentDTO> getLoanInstallmentList(Long loanId) {
        Collection<LoanInstallment> installments = installmentRepo.findByLoanId(loanId);
        List<LoanInstallmentDTO> dtoList = new ArrayList<>();
        installments.forEach(installment -> {
            dtoList.add(modelMapper.map(installment, LoanInstallmentDTO.class));
        });

        return dtoList;
    }

    private void createLoanInstallments(Loan loan, LoanRequestDTO requestDto, final Double oneInstallment) {

        int numberOfInstallment = requestDto.getNumberOfInstallment();

        LocalDate today = LocalDate.now();
        List<LoanInstallment> installmentList = new ArrayList<>();

        for (int i = 0; i < numberOfInstallment; i++) {
            LoanInstallment installment = new LoanInstallment();

            installment.setLoan(loan);
            installment.setAmount(oneInstallment);
            installment.setIsPaid(false);

            //set due date
            LocalDate firstDayOfTheMonth = today.plusMonths(i + 1).withDayOfMonth(1);
            Timestamp timestamp = Timestamp.valueOf(firstDayOfTheMonth.atStartOfDay());
            installment.setDueDate(timestamp);

            //add list
            installmentList.add(installment);
        }
        //save all
        installmentRepo.saveAll(installmentList);
    }

    private boolean checkCustomerCreditLimit(Double creditLimit, Double usedCreditLimit, Double newCreditAmount) {
        double remainedCreditLimit = creditLimit - usedCreditLimit;
        return remainedCreditLimit >= newCreditAmount;
    }

    private Loan createLoanEntity(LoanRequestDTO requestDto) {
        Loan newLoan = new Loan();
        newLoan.setLoanAmount(requestDto.getLoanAmount());
        newLoan.setNumberOfInstallment(requestDto.getNumberOfInstallment());
        newLoan.setIsPaid(false);
        newLoan.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return newLoan;
    }

    /*
    All installments should have same amount. Total amount for loan should be
    amount * (1 + interest rate)
     */
    private Double calculateTotalAmount(LoanRequestDTO requestDto) {
        Double loanAmount = requestDto.getLoanAmount();
        Double interestRate =  requestDto.getInterestRate();
        return loanAmount*(interestRate + 1.0);
    }

}
