# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Story points use shirt sizing to illustrate the point scale given their risk, repetition and complexity. The reference for this can be found on https://asana.com/resources/story-points]


# Admin user stories
## Story 1
**Title:**
_As an [admin], I want [Log into the portal with my username and password], so that [I can manage the platform securely]._

**Acceptance Criteria:**

1. Given a registered user, when they enter an valid username and password that matches an Admin credentials, then they should be redirected to the Admin dashboard.
2. Given a registered user, when they enter an valid username and password that DOES NOT match an Admin credentials, then they should be redirected to the appropiate dashboard according to their role. 
3. Given a registered user, when they enter an incorrect password, then they should see an error message “Invalid username or password.”
4. Given an unregistered user, when they attempt to log in, then they should see an error message “User does not exist.”
5. Given a registered user, when entering a valid username and password, then a session should be created.


**Priority:** [High]
**Story Points:** [L]
**Notes:**


## Story 2

**Title:**
_As an [admin], I want [Log out of the portal], so that [I can protect system access]._

**Acceptance Criteria:**

1. Given a registered user, when they hit the log out button the session will be terminated, then they should be redirected to the Login portal.
2. Given an unregistered user, when they attempt to log out, an error should be displayed in the remote first that they are able to see the "log out" button in the first place 
3. Given an unregistered user, the "log out" button will be replaced by the "log in" one.


**Priority:** [High]
**Story Points:** [M]
**Notes:**


## Story 3

**Title:**
_As an [admin], I want [to Add doctors to the portal], so that [they can be added upon manual and safe verification to manage their profiles]._

**Acceptance Criteria:**

1. Given a previously-verified doctor, the admin will be able to enter the Doctor's credentials and, then, register them via a "register" button
2. Given a non-admin user when they attempt to access this section, an authorization error message will be displayed and the user will be redirected to their dashboard
3. Given an admin when they successfully register a new doctor, this doctor will be added to the Doctor table in MySQL DB


**Priority:** [High]
**Story Points:** [L]
**Notes:**
Authorization may be tricky


## Story 4

**Title:**
_As an [admin], I want [to Delete doctor's profile from the portal], so that [they can be safely removed if need be]._

**Acceptance Criteria:**

1. Given a non-registered user, when their removal is attempted an error message communicates that the user does not exist
2. Given a non-registered doctor, when their removal is attempted an error message communicates that the user is not a doctor does not exist  (optional feature)
3. Given a deleted doctor, said doctor will be removed from the Doctor table in MySQL, or an additional collumn will be used to indicate they are no longer active


**Priority:** [High]
**Story Points:** [M]
**Notes:**
The second acceptance point might not be needed provided Admins can delete any kind of user from the same panel (simpler solution than using multiple forms)


## Story 5

**Title:**
_As an [admin], I want [to Run a stored procedure in MySQL CLI], so that [I get the number of appointments per month and track usage statistics]._

**Acceptance Criteria:**

1. Given a non-registered user, access will be denied and the login portal will be displayed instead
2. Given a non-authorized user, access will be denied stating that permissions are needed for that purpose
3. Given a registered admin, when they request the number of appointments, MongoDB will be accessed by Document Models (Spring Data MongoDB) so the data for each month is displayed


**Priority:** [Medium]
**Story Points:** [L]
**Notes:**




