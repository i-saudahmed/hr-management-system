package com.saud.controller;

import com.saud.dto.request.leave.ApplyLeaveRequest;
import com.saud.dto.response.leave.LeaveResponse;
import com.saud.dto.response.leave.MyLeaveOverviewResponse;
import com.saud.repository.LeaveRepository;
import com.saud.service.LeaveService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Path("/api/leaves")
@Authenticated
public class LeaveController {

    private static final Logger LOG =
            Logger.getLogger(LeaveController.class);

    @Inject
    LeaveService leaveService ;

    @Inject
    JsonWebToken jwt ;

    @POST
    public LeaveResponse applyLeave(ApplyLeaveRequest request){
        System.out.println("Subject = " + jwt.getSubject());
        System.out.println("Claims = " + jwt.getClaimNames());

        Long employeeId = Long.valueOf(jwt.getClaim("employeeId"));
        System.out.println(employeeId + "from controller employeid");
        return  leaveService.appyLeave(employeeId,request);
    }

    @PATCH
    @Path("/{id}/approve/manager")
    public LeaveResponse approvedByManager(@Valid @PathParam("id") Long leaveId){

        Long managerId = Long.valueOf(jwt.getClaim("employeeId"));

        System.out.println(managerId);
        return leaveService.approveByManager(leaveId, managerId);
    }

    @PATCH
    @Path("/{id}/approve/hr")
    public LeaveResponse approveByHr(@Valid  @PathParam("id") Long leaveId){
        Long hrId = Long.valueOf(jwt.getClaim("employeeId").toString());
        System.out.println(hrId);
        return leaveService.approveByHr(leaveId, hrId);
    }

    @GET
    @Path("/my")
    public MyLeaveOverviewResponse getMyLeave() {

        LOG.info("Request received: getMyLeave()");

        Long employeeId = Long.valueOf(jwt.getSubject());

        LOG.debugf("Extracted employeeId from JWT: %s", employeeId);

        MyLeaveOverviewResponse response = leaveService.getMyLeaves(employeeId);

        LOG.infof("Leave data fetched successfully for employeeId: %s", employeeId);

        return response;
    }

    @GET
    @Path("/team")
    public List<LeaveResponse> getTeamLeaves(){
        Long managerId = Long.valueOf(jwt.getSubject());
        System.out.println(managerId);
        return leaveService.getTeamLeaves(managerId);
    }

    @GET
    @Path("/pending")
    public List<LeaveResponse> getPendingLeaves(){
        return  leaveService.getPendingLeaves();
    }
}
