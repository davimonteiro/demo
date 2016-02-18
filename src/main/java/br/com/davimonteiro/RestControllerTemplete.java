package br.com.davimonteiro;

import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * 
 * @author Davi Monteiro Barbosa
 *
 * @param <T>
 * @param <ID>
 */
public abstract class RestControllerTemplete <T, ID extends Serializable> {
	
	private JpaRepository<T, ID> repository;
	
	public RestControllerTemplete(JpaRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	
	//------------------- Create an entity ------------------ //
	
	@ResponseBody
	@RequestMapping(method=POST, consumes={APPLICATION_JSON_VALUE}, produces={APPLICATION_JSON_VALUE})
	public ResponseEntity<T> create(@RequestBody T entity) {
		T result = repository.save(entity);
		return new ResponseEntity<T>(result, HttpStatus.CREATED);
	}
	
	
	//------------------- Retrieve all entity ------------------ //
	
	@RequestMapping(method=GET, produces={APPLICATION_JSON_VALUE})
	@ResponseBody
    public  ResponseEntity<List<T>> findAll() {
        Iterable<T> all = this.repository.findAll();
        List<T> result = Lists.newArrayList(all);
        if(result.isEmpty()){
            return new ResponseEntity<List<T>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<T>>(result, HttpStatus.OK);
    }
	
	
	//------------------- Retrieve one entity ------------------ //
	
	@ResponseBody
	@RequestMapping(value="/{id}", method=GET, produces={APPLICATION_JSON_VALUE})
    public ResponseEntity<T> findById(@PathVariable ID id) {
		T entity = repository.findOne(id);
		if (isNull(entity)) {
            return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<T>(entity, HttpStatus.OK);
    }
	
	
	//------------------- Update an entity ------------------ //
	
	@RequestMapping(value = "/{id}", method = PUT, consumes={APPLICATION_JSON_VALUE}, produces={APPLICATION_JSON_VALUE})
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
         
        T currentEntity = repository.findOne(id);
        
        if (isNull(currentEntity)) {
            return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        }
        
        try {
            BeanUtils.copyProperties(currentEntity, entity);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
 
        repository.save(currentEntity);
        
        return new ResponseEntity<T>(currentEntity, HttpStatus.OK);
    }
	
	
	//------------------- Delete an entity ------------------ //
	
	@RequestMapping(value="/{id}", method=DELETE, produces={APPLICATION_JSON_VALUE})
    public ResponseEntity<T> detele(@PathVariable ID id) {
		repository.delete(id);
		return new ResponseEntity<T>(HttpStatus.NO_CONTENT);
    }

}
