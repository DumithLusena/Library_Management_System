package service.custom.impl;

import dto.MemberDTO;
import entity.Member;
import exceptions.custom.MemberException;
import org.modelmapper.ModelMapper;
import repository.custom.MemberRepository;
import service.custom.MemberService;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberServiceIMPL implements MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public MemberServiceIMPL(ModelMapper mapper,MemberRepository memberRepo) {
        this.modelMapper = mapper;
        this.memberRepository = memberRepo;
    }

    @Override
    public boolean add(MemberDTO member) throws MemberException {
        System.out.println("IMPL : "+member);
        Member entity = convertDTOtoEntity(member);
        try {
            System.out.println("entity : "+entity);
            boolean b = memberRepository.save(entity);
            System.out.println("b : "+b);
            return b;
        } catch (SQLException e) {
            if (e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1062){
                    throw new MemberException("ID Already Exists - Cannot Save.", e);
                } else if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new MemberException("Data is To Large For "+er[1]);
                }
            }
            throw new MemberException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean update(MemberDTO member) throws MemberException {
        Member entity = convertDTOtoEntity(member);
        try {
            boolean isUpdated = memberRepository.update(entity);
            return isUpdated;
        } catch (SQLException e) {
            if(e instanceof SQLException){
                if (((SQLException)e).getErrorCode() == 1406) {
                    String message = ((SQLException) e).getMessage();
                    String[] er = message.split("'");
                    throw new MemberException("Data is To Large For "+er[1]);
                }
            }
            throw new MemberException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public boolean delete(String id) throws MemberException {
        try {
            boolean isDelete = memberRepository.delete(id);
            return isDelete;
        } catch (SQLException  e) {
            e.printStackTrace();
            throw new MemberException("Error Occurred Contact Developer",e);
        }
    }

    @Override
    public Optional<MemberDTO> search(String id) throws MemberException {
        try {
            Optional<Member> member = memberRepository.search(id);
            if (member.isPresent()){
                MemberDTO memberDTO = convertEntitytoDTO(member.get());
               return Optional.of(memberDTO);
            }
        } catch (SQLException e) {
           throw new MemberException("Contact Developer",e);
        }
        return Optional.empty();
    }

    @Override
    public List<MemberDTO> getAll() throws MemberException {
        try {
            List<Member> all = memberRepository.getAll();
            if (all.isEmpty()){
                throw new MemberException("No Members Found");
            }
            List<MemberDTO> memberDTOS = new ArrayList<>();

            for (Member member : all) {
                MemberDTO memberDTO = convertEntitytoDTO(member);
                memberDTOS.add(memberDTO);
            }
            return memberDTOS;

        } catch (SQLException e) {
            throw new MemberException("Contact Develloper",e);
        }
    }

    private Member convertDTOtoEntity(MemberDTO memberDTO){
       return this.modelMapper.map(memberDTO,Member.class);
    }

    private MemberDTO convertEntitytoDTO(Member memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setName(memberEntity.getName());
        memberDTO.setAddress(memberEntity.getAddress());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setContact(memberEntity.getContact());
        return memberDTO;
    }
}
