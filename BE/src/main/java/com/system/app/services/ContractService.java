package com.system.app.services;

import com.system.app.dtos.ContractDto;
import com.system.app.entities.Contract;
import com.system.app.exceptions.EntityNotFoundException;
import com.system.app.models.ContractModel;
import com.system.app.repositories.ContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ContractDto> getAllContracts(String keyword, Pageable pageable) {
        Page<Contract> contracts;
        if (keyword != null && !keyword.isEmpty()) {
            contracts = contractRepository.findByEmployeeID_FullNameContaining(keyword, pageable);
        } else {
            contracts = contractRepository.findAll(pageable);
        }
        return contracts.map(contract -> modelMapper.map(contract, ContractDto.class));
    }

    public ContractDto getContractById(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hợp đồng với ID: " + id));
        return modelMapper.map(contract, ContractDto.class);
    }

    public ContractDto createContract(ContractModel contractModel) {
        if(contractModel.getStartDate().isBefore(Instant.now())){
            throw new RuntimeException("Ngày bắt đầu hợp đồng không hợp lệ");
        }
        if(contractModel.getEndDate().isBefore(contractModel.getStartDate())){
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        Contract contract = modelMapper.map(contractModel, Contract.class);
        contract.setIsActive(false); // Mặc định là không kích hoạt
        Contract savedContract = contractRepository.save(contract);
        return modelMapper.map(savedContract, ContractDto.class);
    }

    public ContractDto updateContract(Integer id, ContractModel contractModel) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hợp đồng với ID: " + id));

        modelMapper.map(contractModel, existingContract);
        Contract updatedContract = contractRepository.save(existingContract);
        return modelMapper.map(updatedContract, ContractDto.class);
    }

    @Transactional
    public void deleteContract(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hợp đồng với ID: " + id));
        contractRepository.delete(contract);
    }

    public ContractDto toggleContractStatus(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hợp đồng với ID: " + id));
        contract.setIsActive(!contract.getIsActive());
        Contract updatedContract = contractRepository.save(contract);
        return modelMapper.map(updatedContract, ContractDto.class);
    }
}
