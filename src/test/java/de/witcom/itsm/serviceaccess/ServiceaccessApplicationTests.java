package de.witcom.itsm.serviceaccess;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.witcom.itsm.serviceaccess.entity.*;
import de.witcom.itsm.serviceaccess.enums.ResourceType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.repository.*;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;


@SpringBootTest
@Transactional
class ServiceaccessApplicationTests {


	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	EntityManager entityManager;

	
	@Autowired
	ServiceAccessRepository saRepo;

	@Autowired
	ServiceAccessSubtypeRepository subtypeRepo;

	@Autowired
	ServiceAccessService saService;

	
	@BeforeAll
	public static void init(@Autowired ServiceAccessSubtypeRepository subtypeRepo) {

		ServiceAccessSubtype st = ServiceAccessSubtype.builder()
				.name("HA-LWL")
				.description("LWL Hausanschluss")
				.numberOfEndpoints(1)
				.scope(ServiceAccessScope.UNSCOPED)
				.offerScope(ServiceAccessOfferScope.UNSCOPED)
				.allowedObjectType(ServiceAccessObjectType.InfraPassive)
				.build();
		st = subtypeRepo.save(st);

		st =  ServiceAccessSubtype.builder()
				.name("ETHERNET-WHOLEBUY")
				.description("Ethernet Fremdoperator")
				.numberOfEndpoints(2)
				.allowedObjectType(ServiceAccessObjectType.InfraOtherOperator)
				.scope(ServiceAccessScope.SERVICE)
				.offerScope(ServiceAccessOfferScope.SERVICE)
				.build();

		subtypeRepo.save(st);

	}
	
	ServiceAccessInfraOtherOperator createOOEntity() {

    	ServiceAccessSubtype ewb = subtypeRepo.findOneByName("ETHERNET-WHOLEBUY").orElseThrow(IllegalArgumentException::new);

		HashSet<ResourceReference> ooRes = new HashSet<ResourceReference>();
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId(RandomStringUtils.random(10, true, true))
				.description("A-ENDE")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId(RandomStringUtils.random(10, true, true))
				.description("B-ENDE")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_CONTRACT)
				.referenceId(RandomStringUtils.random(10, true, true))
				.description("Vertrag")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.CRM_CONTACT)
				.referenceId(RandomStringUtils.random(10, true, true))
				.description("Lieferant")
				.build());
		
		ServiceAccessInfraOtherOperator infraOO = ServiceAccessInfraOtherOperator.builder()
				.name(RandomStringUtils.random(10, true, false))
				.resources(ooRes)
				.subType(ewb)

				.constraints("100 mbits max")
				.build();

		return infraOO;
	}
	
	
	ServiceAccessInfraPassive createInfraPassiveEntity() {

		ServiceAccessSubtype st =  subtypeRepo.findOneByName("HA-LWL").orElseThrow(IllegalArgumentException::new);

		ServiceAccessInfraPassive infraPassive1 = ServiceAccessInfraPassive.builder()
				.name(RandomStringUtils.random(10, true, false))
				.subType(st)
				.build();
		
		infraPassive1.getResources().add(ResourceReference.builder()
				.type(ResourceType.RMDB_OBJECT)
				.referenceId(RandomStringUtils.random(10, true, true))
				.build()
				);
		
		return infraPassive1;
	}
	
	@Test
	void emTests() {
		
		ServiceAccessInfraPassive infraPassive = this.createInfraPassiveEntity();
		saRepo.save(infraPassive);
		infraPassive = this.createInfraPassiveEntity();
		saRepo.save(infraPassive);
		ServiceAccessInfraOtherOperator infraOO1 = this.createOOEntity();
		saRepo.save(infraOO1);
		
		
		//infraPassive1.getSubType().
		
		List<ServiceAccessSubtype> st = entityManager.createQuery("SELECT st from ServiceAccessSubtype st").getResultList();
		log.debug(st.toString());
		
		List<ServiceAccessBase> filteredRes = entityManager.createQuery("SELECT sa from ServiceAccessBase sa where sa.subType = ?1")
	      .setParameter(1, infraOO1.getSubType())
	      .getResultList();
		
		log.debug(filteredRes.toString());
		
		List<ServiceAccessBase> filtered = saRepo.findBySubType(infraOO1.getSubType());
		log.debug(filtered.toString());
		
		List<ServiceAccessBase> res = entityManager.createQuery("SELECT sa from ServiceAccessBase sa").getResultList();
		log.debug(res.toString());
		
	}
    
    @Test
    void groupTests()  {
    	ServiceAccessSubtype ewb = subtypeRepo.findOneByName("ETHERNET-WHOLEBUY").orElseThrow(IllegalArgumentException::new);

    	
    	ServiceAccessInfraOtherOperator infraOO1 = this.createOOEntity();
    	infraOO1 = saRepo.save(infraOO1);
    	ServiceAccessInfraOtherOperator infraOO2 = this.createOOEntity();
    	infraOO2 = saRepo.save(infraOO2);

    	//group it
		HashSet<ServiceAccessInfraOtherOperator> members= new HashSet<ServiceAccessInfraOtherOperator>();
		members.add(infraOO1);
		members.add(infraOO2);
		
		assertThat(members, hasSize(2));
		
		HashSet<ResourceReference> groupRes = new HashSet<ResourceReference>();
		groupRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId("command-elid-1")
				.description("A-ENDE")
				.build());
		groupRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId("command-elid-5")
				.description("B-ENDE")
				.build());
		
		ServiceAccessOtherOperatorGroup group = ServiceAccessOtherOperatorGroup.builder()
				.name("MY-OO-GROUP")
				.subType(ewb)
				.otherOperators(members)
				.resources(groupRes)
				.build();
		group = saRepo.save(group);
		assertThat(group.getOtherOperators(), hasSize(2));
    	
    }

	@Test
	void basicTests() {
		
		ServiceAccessInfraPassive infraPassive1 = this.createInfraPassiveEntity();
		infraPassive1=saRepo.save(infraPassive1);
		
		ServiceAccessInfraOtherOperator infraOO = this.createOOEntity();
		infraOO = saRepo.save(infraOO);

		List<ServiceAccessBase> sas = saRepo.findAll();
		assertThat(sas,hasSize(2));

		ServiceAccessBase access = saRepo.getOne(infraOO.getId());
		if (access instanceof ServiceAccessInfraOtherOperator) {
			ServiceAccessInfraOtherOperator infraOOLoaded = (ServiceAccessInfraOtherOperator) access;
			assertThat(infraOO,is(infraOOLoaded));
			//this.log.debug(infraOO.getId());

		}
		
		ServiceAccessBase entity = saRepo.findById(infraPassive1.getId()).get();

		//delete tests - should work
		saRepo.delete(access);
		saRepo.delete(infraPassive1);

		//should fail
		//subtypeRepo.delete(st);
		
		log.debug("Anzahl Subtypes {}",subtypeRepo.count());
		log.debug("Anzahl SA {}",saRepo.count());

	}

}
