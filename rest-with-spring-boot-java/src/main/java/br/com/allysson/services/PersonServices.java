package br.com.allysson.services;

import br.com.allysson.data.dto.V1.PersonDTO;
import br.com.allysson.data.dto.V2.PersonDTOV2;
import br.com.allysson.exception.ResourceNotFoundException;
import static br.com.allysson.mapper.ObjectMapper.parseListObjects;
import static br.com.allysson.mapper.ObjectMapper.parseObject;

import br.com.allysson.mapper.custom.PersonMapper;
import br.com.allysson.model.Person;
import br.com.allysson.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper converter;

    public List<PersonDTO> findAll(){
        logger.info("Finding all Person! / Pegando todos os Id da Tabela...");

        return parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO findBydId(Long id){

        logger.info("Finding one Person! / Pegando só um Id");

         var entity = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this id / Não foi encontrado o ID"));
         return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person){
        logger.info("Creando o PERSON no PATH person!");

        var entity = parseObject(person, Person.class);

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTOV2 createV2(PersonDTOV2 person){
        logger.info("Creando o PERSONDTOV2 no PATH personV2!");

        var entity = converter.convertTODtoEntity(person);

        return converter.convertEntityTODTO(repository.save(entity));
    }

    public PersonDTO update(PersonDTO person){
        logger.info("Atualizando o person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this id / Id não encontrado"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id){
        logger.info("Deleting one Person! / Id Deletado com sucesso!");
        Person entity = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No records found for this id"));

        repository.delete(entity);
    }

}
