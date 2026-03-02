document.getElementById("loanType").addEventListener("change", function () {
  const loanType = this.value;
  const interestField = document.getElementById("interest");

  if (loanType === "HOME") {
    interestField.value = 9;
  } else if (loanType === "CAR") {
    interestField.value = 12;
  } else if (loanType === "PERSONAL") {
    interestField.value = 15;
  } else {
    interestField.value = "";
  }
});

function principalChecker() {
  const loanType = document.getElementById("loanType").value;
  const principalInput = document.getElementById("amount");
  const errorDiv = document.getElementById("error");

  const principal = parseFloat(principalInput.value);

  errorDiv.innerText = "";
  principalInput.classList.remove("input-error");

  if (loanType === "HOME" && principal < 500000) {
    errorDiv.innerText = "Home loan minimum amount is 500000";
    principalInput.classList.add("input-error");
    return;
  }

  if (loanType === "CAR" && principal < 100000) {
    errorDiv.innerText = "Car loan minimum amount is 100000";
    principalInput.classList.add("input-error");
    return;
  }

  if (loanType === "PERSONAL" && principal < 10000) {
    errorDiv.innerText = "Personal loan minimum amount is 10000";
    principalInput.classList.add("input-error");
    return;
  }
}

function durationChecker() {
  const loanType = document.getElementById("loanType").value;
  const durationInput = document.getElementById("duration");
  const durationYears = parseFloat(durationInput);
  const errorDiv = document.getElementById("error");

  errorDiv.innerText = "";
  durationInput.classList.remove("input-error");

  if (loanType === "HOME" && durationYears > 30) {
    errorDiv.innerText = "Home loan maximum duration is 30y";
    durationInput.classList.add("input-error");
    return;
  }

  if (loanType === "CAR" && durationYears > 10) {
    errorDiv.innerText = "Car loan maximum duration is 10y";
    durationInput.classList.add("input-error");
    return;
  }

  if (loanType === "PERSONAL" && durationYears > 5) {
    errorDiv.innerText = "Personal loan maximum duration is 5y";
    durationInput.classList.add("input-error");
    return;
  }
}

function calculateEMI() {
  const loanType = document.getElementById("loanType").value;
  const principal = parseFloat(document.getElementById("amount").value);
  const durationYears = parseFloat(document.getElementById("duration").value);
  const interestRate = parseFloat(document.getElementById("interest").value);
  const errorDiv = document.getElementById("error");
  const applicantName = document.getElementById("name").value;

  errorDiv.innerText = "";

  if (!applicantName || !loanType || !principal || !durationYears) {
    errorDiv.innerText = "Some field ismissing";
    return;
  }

  if (applicantName.length < 5) {
    errorDiv.innerText = "Please enter your full name!";
    return;
  }

  if (loanType === "HOME") {
    if (principal < 500000) {
      errorDiv.innerText = "Home loan minimum amount is 500000";
      return;
    }
    if (durationYears > 30) {
      errorDiv.innerText = "Home loan maximum duration is 30 years";
      return;
    }
  }

  if (loanType === "CAR") {
    if (principal < 100000) {
      errorDiv.innerText = "Car loan minimum amount is 100000";
      return;
    }
    if (durationYears > 7) {
      errorDiv.innerText = "Car loan maximum duration is 7 years";
      return;
    }
  }

  if (loanType === "PERSONAL") {
    if (principal < 10000) {
      errorDiv.innerText = "Personal loan minimum amount is 10000";
      return;
    }
    if (durationYears > 5) {
      errorDiv.innerText = "Personal loan maximum duration is 5 years";
      return;
    }
  }

  const monthlyInterest = interestRate / 12 / 100;
  const months = durationYears * 12;

  const emi = (principal * monthlyInterest * Math.pow(1 + monthlyInterest, months)) / (Math.pow(1 + monthlyInterest, months) - 1);
  const totalPayable = emi + principal;

  document.getElementById("emi").value = emi;
  document.getElementById("totalPayable").value = totalPayable;

  console.log("Applicant " + applicantName + " pays " + emi + " EMI on their " + loanType + " loan");
}
