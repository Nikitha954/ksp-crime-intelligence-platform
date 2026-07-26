import os
import json
import random
from datetime import datetime, timedelta

def generate_ksp_data():
    print("Generating KSP Crime Analytics Synthetic Dataset (~3000 records)...")
    
    # 1. Master Lookup Arrays
    districts = [
        {"DistrictID": 1001, "DistrictName": "Bengaluru Urban", "StateID": 29, "lat": 12.9716, "lon": 77.5946},
        {"DistrictID": 1002, "DistrictName": "Bengaluru Rural", "StateID": 29, "lat": 13.2172, "lon": 77.6271},
        {"DistrictID": 1003, "DistrictName": "Mysuru", "StateID": 29, "lat": 12.2958, "lon": 76.6394},
        {"DistrictID": 1004, "DistrictName": "Mangaluru (Dakshina Kannada)", "StateID": 29, "lat": 12.9141, "lon": 74.8560},
        {"DistrictID": 1005, "DistrictName": "Belagavi", "StateID": 29, "lat": 15.8497, "lon": 74.4977},
        {"DistrictID": 1006, "DistrictName": "Hubballi-Dharwad", "StateID": 29, "lat": 15.3647, "lon": 75.1240},
        {"DistrictID": 1007, "DistrictName": "Tumakuru", "StateID": 29, "lat": 13.3379, "lon": 77.1173},
        {"DistrictID": 1008, "DistrictName": "Ballari", "StateID": 29, "lat": 15.1394, "lon": 76.9214},
        {"DistrictID": 1009, "DistrictName": "Shivamogga", "StateID": 29, "lat": 13.9299, "lon": 75.5681},
        {"DistrictID": 1010, "DistrictName": "Kalaburagi", "StateID": 29, "lat": 17.3297, "lon": 76.8343}
    ]

    units_by_district = {
        1001: [
            {"UnitID": 5001, "UnitName": "Central PS"}, {"UnitID": 5002, "UnitName": "Cyber Crime PS"},
            {"UnitID": 5003, "UnitName": "Indiranagar PS"}, {"UnitID": 5004, "UnitName": "Koramangala PS"},
            {"UnitID": 5005, "UnitName": "Whitefield PS"}, {"UnitID": 5006, "UnitName": "Electronic City PS"},
            {"UnitID": 5007, "UnitName": "Jayanagar PS"}, {"UnitID": 5008, "UnitName": "Yelahanka PS"}
        ],
        1002: [
            {"UnitID": 5009, "UnitName": "Nelamangala PS"}, {"UnitID": 5010, "UnitName": "Hoskote PS"},
            {"UnitID": 5011, "UnitName": "Devanahalli PS"}, {"UnitID": 5012, "UnitName": "Doddaballapura PS"}
        ],
        1003: [
            {"UnitID": 5013, "UnitName": "Devaraja PS"}, {"UnitID": 5014, "UnitName": "Saraswathipuram PS"},
            {"UnitID": 5015, "UnitName": "Nazarbad PS"}, {"UnitID": 5016, "UnitName": "Kuvempunagar PS"}
        ],
        1004: [
            {"UnitID": 5017, "UnitName": "Urwa PS"}, {"UnitID": 5018, "UnitName": "Pandeshwar PS"},
            {"UnitID": 5019, "UnitName": "Kadri PS"}, {"UnitID": 5020, "UnitName": "Panambur PS"}
        ],
        1005: [
            {"UnitID": 5021, "UnitName": "Camp PS"}, {"UnitID": 5022, "UnitName": "Market PS"},
            {"UnitID": 5023, "UnitName": "Shahapur PS"}, {"UnitID": 5024, "UnitName": "Khade Bazar PS"}
        ],
        1006: [
            {"UnitID": 5025, "UnitName": "Subhash Nagar PS"}, {"UnitID": 5026, "UnitName": "Town PS"},
            {"UnitID": 5027, "UnitName": "Gokul Road PS"}, {"UnitID": 5028, "UnitName": "Vidyanagar PS"}
        ],
        1007: [
            {"UnitID": 5029, "UnitName": "Tumakuru Town PS"}, {"UnitID": 5030, "UnitName": "New Extension PS"},
            {"UnitID": 5031, "UnitName": "Kyathsandra PS"}
        ],
        1008: [
            {"UnitID": 5032, "UnitName": "Brucepet PS"}, {"UnitID": 5033, "UnitName": "Cowl Bazar PS"},
            {"UnitID": 5034, "UnitName": "Gandhinagar PS"}
        ],
        1009: [
            {"UnitID": 5035, "UnitName": "Doddapete PS"}, {"UnitID": 5036, "UnitName": "Kote PS"},
            {"UnitID": 5037, "UnitName": "Tunga Nagar PS"}
        ],
        1010: [
            {"UnitID": 5038, "UnitName": "Station Bazaar PS"}, {"UnitID": 5039, "UnitName": "Brahmpur PS"},
            {"UnitID": 5040, "UnitName": "Chowk PS"}
        ]
    }

    crime_heads = [
        {"CrimeHeadID": 101, "CrimeGroupName": "Crimes Against Property"},
        {"CrimeHeadID": 102, "CrimeGroupName": "Crimes Against Body / Person"},
        {"CrimeHeadID": 103, "CrimeGroupName": "Cyber Crimes"},
        {"CrimeHeadID": 104, "CrimeGroupName": "Economic Offence / White Collar"},
        {"CrimeHeadID": 105, "CrimeGroupName": "Special & Local Laws (SLL)"}
    ]

    crime_subheads = [
        {"CrimeSubHeadID": 201, "CrimeHeadID": 101, "CrimeHeadName": "Theft (MVT & General)"},
        {"CrimeSubHeadID": 202, "CrimeHeadID": 101, "CrimeHeadName": "Burglary & House Breaking"},
        {"CrimeSubHeadID": 203, "CrimeHeadID": 101, "CrimeHeadName": "Robbery"},
        {"CrimeSubHeadID": 204, "CrimeHeadID": 101, "CrimeHeadName": "Dacoity"},
        {"CrimeSubHeadID": 205, "CrimeHeadID": 102, "CrimeHeadName": "Grievous Hurt & Assault"},
        {"CrimeSubHeadID": 206, "CrimeHeadID": 102, "CrimeHeadName": "Kidnapping & Abduction"},
        {"CrimeSubHeadID": 207, "CrimeHeadID": 102, "CrimeHeadName": "Attempt to Murder"},
        {"CrimeSubHeadID": 208, "CrimeHeadID": 103, "CrimeHeadName": "Online Financial Fraud / UPI Scam"},
        {"CrimeSubHeadID": 209, "CrimeHeadID": 103, "CrimeHeadName": "Identity Theft & Phishing"},
        {"CrimeSubHeadID": 210, "CrimeHeadID": 104, "CrimeHeadName": "Cheating & Criminal Breach of Trust"},
        {"CrimeSubHeadID": 211, "CrimeHeadID": 104, "CrimeHeadName": "Forgery & Document Fraud"},
        {"CrimeSubHeadID": 212, "CrimeHeadID": 105, "CrimeHeadName": "NDPS (Narcotics)"},
        {"CrimeSubHeadID": 213, "CrimeHeadID": 105, "CrimeHeadName": "Arms Act Violation"}
    ]

    case_categories = [
        {"CaseCategoryID": 1, "LookupValue": "FIR"},
        {"CaseCategoryID": 3, "LookupValue": "UDR"},
        {"CaseCategoryID": 8, "LookupValue": "Zero FIR"},
        {"CaseCategoryID": 4, "LookupValue": "PAR"}
    ]

    case_statuses = [
        {"CaseStatusID": 1, "CaseStatusName": "Under Investigation"},
        {"CaseStatusID": 2, "CaseStatusName": "Chargesheeted"},
        {"CaseStatusID": 3, "CaseStatusName": "Closed / Final Report"}
    ]

    gravity_offences = [
        {"GravityOffenceID": 1, "LookupValue": "Heinous"},
        {"GravityOffenceID": 2, "LookupValue": "Non-Heinous"}
    ]

    # Repeat offender personas to bind across multiple cases for network graph
    repeat_accused_pool = [
        {"PersonID": "KA-ACC-101", "AccusedName": "Ramesh 'Tiger' Kumar", "AgeYear": 34, "GenderID": "Male"},
        {"PersonID": "KA-ACC-102", "AccusedName": "Syed Imran", "AgeYear": 29, "GenderID": "Male"},
        {"PersonID": "KA-ACC-103", "AccusedName": "Manjunath 'Bullet' Gowda", "AgeYear": 38, "GenderID": "Male"},
        {"PersonID": "KA-ACC-104", "AccusedName": "Priya Sharma", "AgeYear": 31, "GenderID": "Female"},
        {"PersonID": "KA-ACC-105", "AccusedName": "Suresh Naik", "AgeYear": 42, "GenderID": "Male"},
        {"PersonID": "KA-ACC-106", "AccusedName": "Anand 'Snake' Murthy", "AgeYear": 27, "GenderID": "Male"},
        {"PersonID": "KA-ACC-107", "AccusedName": "Venkatesh Prasad", "AgeYear": 45, "GenderID": "Male"},
        {"PersonID": "KA-ACC-108", "AccusedName": "Deepak Reddy", "AgeYear": 33, "GenderID": "Male"}
    ]

    first_names = ["Arun", "Kiran", "Vijay", "Santhosh", "Ganesh", "Mahesh", "Sunil", "Pooja", "Meena", "Rashmi", "Deepa", "Lakshmi", "Rajesh", "Prakash", "Shivakumar", "Basavaraj", "Siddaramaiah", "Jagadish", "Babu", "Shivaraj"]
    last_names = ["Rao", "Hegde", "Bhat", "Shetty", "Gowda", "Patil", "Kulkarni", "Deshmukh", "Pujari", "Reddy", "Chavhan", "Jadhav", "Kuruba", "Nayak", "Kambale"]

    sample_facts = [
        "Complainant reported theft of two-wheeler parked in front of residential house overnight.",
        "Accused broke open front door lock using iron rod and looted gold ornaments weighing 45 grams.",
        "Victim received fake electricity bill payment link via SMS and lost Rs. 85,000 via unauthorized UPI transfer.",
        "Physical altercation broke out near tea stall leading to grievous hurt inflicted by sharp object.",
        "Cyber crime reported regarding fake job offer letter demanding processing fee deposit of Rs. 35,000.",
        "Commercial establishment broken into overnight; cash drawer tampered and electronics stolen.",
        "Seizure of illegal narcotic substance (Ganja) weighing 2.5 kg from suspect during routine patrol check.",
        "Cheating complaint filed regarding fraudulent real estate plot booking without clear title deed.",
        "Accused threatened complainant with illegal firearm demanding protection money.",
        "Chain snatching incident reported by pedestrian walking home in late evening."
    ]

    now = datetime.now()
    start_date = now - timedelta(days=730) # 24 months
    
    # Running serial map for CrimeNo generation per unit/year
    serial_counters = {}

    cases = []
    victims = []
    accused_list = []
    complainants = []
    arrests = []

    case_id_counter = 1
    victim_id_counter = 1
    accused_id_counter = 1
    complainant_id_counter = 1
    arrest_id_counter = 1

    total_records = 3000

    # Emerging hotspots boost: set high density in recent 30 days for Bengaluru Urban (Whitefield PS 5005 & Koramangala 5004) and Mysuru (Devaraja PS 5013)
    for i in range(total_records):
        # District selection (Bengaluru Urban weighted 35%, Mysuru 15%, Mangaluru 10%, others equal)
        dist_choice = random.choices(
            districts,
            weights=[35, 8, 15, 10, 8, 8, 4, 4, 4, 4],
            k=1
        )[0]
        
        district_id = dist_choice["DistrictID"]
        units = units_by_district[district_id]
        unit = random.choice(units)
        unit_id = unit["UnitID"]

        # Date generation with seasonal/weekend clustering
        # Give ~25% chance of being in the recent 30 days to trigger "emerging hotspot" trends
        if random.random() < 0.25:
            days_ago = random.randint(1, 30)
        else:
            days_ago = random.randint(31, 730)

        incident_dt = now - timedelta(days=days_ago, hours=random.randint(0, 23), minutes=random.randint(0, 59))
        reg_dt = incident_dt + timedelta(hours=random.randint(1, 48))
        
        year = reg_dt.year
        cat = random.choices(case_categories, weights=[85, 5, 5, 5], k=1)[0]
        cat_code = cat["CaseCategoryID"]

        key = (cat_code, district_id, unit_id, year)
        serial_counters[key] = serial_counters.get(key, 0) + 1
        serial = serial_counters[key]

        # KSP CrimeNo: CatCode (1) + DistID (4) + UnitID (4) + Year (4) + Serial (5)
        crime_no = f"{cat_code}{district_id:04d}{unit_id:04d}{year:04d}{serial:05d}"
        case_no = f"{year:04d}{serial:05d}"

        subhead = random.choice(crime_subheads)
        majorhead_id = subhead["CrimeHeadID"]
        minorhead_id = subhead["CrimeSubHeadID"]

        # Lat/Lon with small Gaussian jitter around district centroid
        lat = round(dist_choice["lat"] + random.gauss(0, 0.03), 6)
        lon = round(dist_choice["lon"] + random.gauss(0, 0.03), 6)

        status = random.choices(case_statuses, weights=[50, 40, 10], k=1)[0]
        gravity = random.choices(gravity_offences, weights=[25, 75], k=1)[0]

        brief = random.choice(sample_facts)

        case_row = {
            "CaseMasterID": case_id_counter,
            "CrimeNo": crime_no,
            "CaseNo": case_no,
            "CrimeRegisteredDate": reg_dt.strftime("%Y-%m-%d"),
            "PoliceStationID": unit_id,
            "PoliceStationName": unit["UnitName"],
            "DistrictID": district_id,
            "DistrictName": dist_choice["DistrictName"],
            "CaseCategoryID": cat_code,
            "CaseCategoryName": cat["LookupValue"],
            "GravityOffenceID": gravity["GravityOffenceID"],
            "GravityOffenceName": gravity["LookupValue"],
            "CrimeMajorHeadID": majorhead_id,
            "CrimeMinorHeadID": minorhead_id,
            "CrimeSubHeadName": subhead["CrimeHeadName"],
            "CaseStatusID": status["CaseStatusID"],
            "CaseStatusName": status["CaseStatusName"],
            "IncidentFromDate": incident_dt.strftime("%Y-%m-%d %H:%M:%S"),
            "IncidentToDate": (incident_dt + timedelta(hours=2)).strftime("%Y-%m-%d %H:%M:%S"),
            "InfoReceivedPSDate": reg_dt.strftime("%Y-%m-%d %H:%M:%S"),
            "latitude": lat,
            "longitude": lon,
            "BriefFacts": brief
        }
        cases.append(case_row)

        # 2. Victim details
        vic_name = f"{random.choice(first_names)} {random.choice(last_names)}"
        victims.append({
            "VictimMasterID": victim_id_counter,
            "CaseMasterID": case_id_counter,
            "VictimName": vic_name,
            "AgeYear": random.randint(18, 70),
            "GenderID": random.choice(["Male", "Female"]),
            "VictimPolice": False
        })
        victim_id_counter += 1

        # 3. Complainant details (Socio-demographic data stored safely on complainant/victim side only)
        comp_name = vic_name if random.random() < 0.7 else f"{random.choice(first_names)} {random.choice(last_names)}"
        complainants.append({
            "ComplainantID": complainant_id_counter,
            "CaseMasterID": case_id_counter,
            "ComplainantName": comp_name,
            "AgeYear": random.randint(21, 65),
            "GenderID": random.choice(["Male", "Female"]),
            "OccupationID": random.choice(["IT Professional", "Businessman", "Government Employee", "Student", "Home Maker", "Farmer"]),
            "ReligionID": random.choice(["Hindu", "Muslim", "Christian", "Jain", "Sikh"]),
            "CasteID": random.choice(["General", "OBC", "SC", "ST"])
        })
        complainant_id_counter += 1

        # 4. Accused details (Bind repeat offenders to multiple cases for force-graph)
        num_accused = 1 if random.random() < 0.7 else 2
        for a_idx in range(num_accused):
            if random.random() < 0.35 and len(repeat_accused_pool) > 0:
                # Pick repeat offender
                rep = random.choice(repeat_accused_pool)
                acc_name = rep["AccusedName"]
                p_id = rep["PersonID"]
                age = rep["AgeYear"]
                gender = rep["GenderID"]
            else:
                acc_name = f"{random.choice(first_names)} {random.choice(last_names)}"
                p_id = f"A{accused_id_counter}"
                age = random.randint(19, 55)
                gender = "Male" if random.random() < 0.9 else "Female"

            acc_row = {
                "AccusedMasterID": accused_id_counter,
                "CaseMasterID": case_id_counter,
                "AccusedName": acc_name,
                "AgeYear": age,
                "GenderID": gender,
                "PersonID": p_id
            }
            accused_list.append(acc_row)

            # Arrest surrender row
            if status["CaseStatusID"] in [2, 3] or random.random() < 0.4:
                arrests.append({
                    "ArrestSurrenderID": arrest_id_counter,
                    "CaseMasterID": case_id_counter,
                    "AccusedMasterID": accused_id_counter,
                    "ArrestSurrenderTypeID": 1,
                    "ArrestSurrenderDate": (reg_dt + timedelta(days=random.randint(1, 15))).strftime("%Y-%m-%d"),
                    "PoliceStationID": unit_id,
                    "IOID": f"IO-OFFICER-{random.randint(101, 150)}"
                })
                arrest_id_counter += 1

            accused_id_counter += 1

        case_id_counter += 1

    # Format into cohesive dataset payload
    dataset = {
        "districts": districts,
        "crimeHeads": crime_heads,
        "crimeSubHeads": crime_subheads,
        "caseCategories": case_categories,
        "caseStatuses": case_statuses,
        "gravityOffences": gravity_offences,
        "repeatAccusedPool": repeat_accused_pool,
        "cases": cases,
        "victims": victims,
        "complainants": complainants,
        "accused": accused_list,
        "arrests": arrests
    }

    out_dir = os.path.join(os.path.dirname(__file__), "..", "data")
    os.makedirs(out_dir, exist_ok=True)
    out_file = os.path.join(out_dir, "ksp_crime_data.json")

    with open(out_file, "w", encoding="utf-8") as f:
        json.dump(dataset, f, indent=2)

    print(f"Dataset successfully created with {len(cases)} cases at: {out_file}")

if __name__ == "__main__":
    generate_ksp_data()
