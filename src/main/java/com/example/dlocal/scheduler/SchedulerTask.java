package com.example.dlocal.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.dlocal.HibernateUtil;
import com.example.dlocal.dao.SaleDao;
import com.example.dlocal.model.Sale;

@Component
public class SchedulerTask {

	@Autowired
	SaleDao saleDao;

	Map<Integer, Boolean> probRejectOrPaidMap;

	@Scheduled(fixedRate = 30000)
	public void start() {

		// Get the pending records and update them
		builProbabiltyMap();

		// main method, update the records.
		updateRecords();
	}

	/**
	 * Update Batch of records.
	 */
	@Transactional
	public void updateRecords() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		// get the records of status PENDING (1)
		ScrollableResults saleCursor = session.createQuery(
				"from Sale where status=1").scroll();
		int count = 0;

		while (saleCursor.next()) {

			Sale sale = (Sale) saleCursor.get(0);

			// set to PAID(2) in 70% of the cases and REJECTED(3) in 30%.
			sale.setStatus(isPaidOrReject(count));
			session.update(sale);

			// update to db in bulk of 10
			if (++count % 10 == 0) {
				session.flush();
				session.clear();
				count = 0;
			}
		}
		tx.commit();
		session.close();
		return;
	}

	/**
	 * if the index of the current record is in the paidRejectedRows map, return
	 * 3 (REJECTED)
	 * 
	 * @param indexOfRecords
	 *            - the record index
	 */
	private Short isPaidOrReject(int indexOfRecords) {

		// if the probability map contain the given index, we will set it to
		// REJECTED(3)
		return (short) (probRejectOrPaidMap.containsKey(indexOfRecords) ? 3 : 2);
	}

	/**
	 * Building at the first time a map of 3 indexes that represent "REJECTED"
	 * stauts. Set 3 random numbers from 0-9 in which the corresponding records
	 * indexes from DB be set to REJECTED
	 */
	private void builProbabiltyMap() {

		if (probRejectOrPaidMap == null) {

			probRejectOrPaidMap = new HashMap<Integer, Boolean>();

			// create array of 10 numbers from 0-9
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < 10; i++) {
				list.add(new Integer(i));
			}
			// shuffle it to gain equal probability of 70%-30% by getting the 3
			// elements.
			Collections.shuffle(list);
			for (int i = 0; i < 3; i++) {
				probRejectOrPaidMap.put(list.get(i), true);
			}
		}
	}
}
