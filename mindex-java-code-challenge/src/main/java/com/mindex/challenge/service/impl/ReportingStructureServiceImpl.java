package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure currentReportingStructure = new ReportingStructure(employee);

        currentReportingStructure.setNumberOfReports(countReports(employee));

        return currentReportingStructure;
    }

    private int countReports(Employee employee) {
        List<Employee> reports = employee.getDirectReports();

        if(reports == null || reports.isEmpty()) {
            return 0;
        }

        int numberOfReports = reports.size();

        for (Employee e : reports) {
            numberOfReports += countReports(employeeRepository.findByEmployeeId(e.getEmployeeId()));
        }

        return numberOfReports;
    }
}
