#include <bits/stdc++.h>
#define Date "02/06/2025"
using namespace std;

class Train {
private:
    int id;
    string starting;
    string destination;
    bool available;
public:
    Train(int id_, string start, string dest, bool avail = true) {
        id = id_;
        starting = start;
        destination = dest;
        available = avail;
    }
    int getId() const { return id; }
    string getStart() const { return starting; }
    string getDest() const { return destination; }
    bool isAvailable() const { return available; }
    void setAvailable(bool avail) { available = avail; }
    void display() const {
        cout << "Train ID: " << id << ", From: " << starting << ", To: " << destination << ", Date: " << Date << ", Available: " << (available ? "Yes" : "No") << endl;
    }
};

class Booking {
public:
    virtual void displayBookingDetails() = 0;
    virtual void calculateFare() = 0;
    virtual void bookTrain(int id, vector<Train> &trains) = 0; 
    virtual ~Booking() {}
};

class Reservation : public Booking {
private:
    int bookedTrainId = -1;
    string userName;
public:
    Reservation(string user) : userName(user) {}
    int searchTrain(int id, vector<Train> &trains) {
        for (int i = 0; i < trains.size(); ++i) {
            if (trains[i].getId() == id && trains[i].isAvailable()) {
                trains[i].display();
                return i;
            }
        }
        cout << "Train not found or not available!" << endl;
        return -1;
    }
    void bookTrain(int id, vector<Train> &trains) override {
        int idx = searchTrain(id, trains);
        if (idx != -1) {
            trains[idx].setAvailable(false);
            bookedTrainId = id;
            cout << "Train booked successfully!" << endl;
        }
    }
    void displayBookingDetails() override {
        if (bookedTrainId != -1)
            cout << "User: " << userName << ", Booked Train ID: " << bookedTrainId << endl;
        else
            cout << "No booking found for user: " << userName << endl;
    }
    void calculateFare() override {
        cout << "Fare calculation not implemented." << endl;
    }
};

class User {
    int userId;
    string name;
    string password;
    vector<pair<int, int>> bookedTrains;
public:
    User(int id, string n, string pass) {
        userId = id;
        name = n;
        password = pass;
    }
    string getName() { return name; }
    int getId() { return userId; }
    string getPassword() { return password; }
    void addBooking(int trainId, int resId) {
        bookedTrains.push_back({trainId, resId});
    }
    void viewBookings() {
        cout << "User: " << name << " Booked Trains: ";
        for (auto &p : bookedTrains)
            cout << p.first << " ";
        cout << endl;
    }
};

vector<User> users;
vector<Train> availableTrains;

void initializeTrains() {
    availableTrains.push_back(Train(101, "Kolkata", "Delhi", true));
    availableTrains.push_back(Train(102, "Mumbai", "Chennai", true));
    availableTrains.push_back(Train(103, "Delhi", "Bangalore", true));
}

void registerUser(vector<User> &users) {
    int id; string name, password;
    cout << "Enter id: ";
    cin >> id;
    cout << "Enter your name: ";
    cin >> name;
    cout << "Enter your password: ";
    cin >> password;
    for (auto &u : users) {
        if (u.getId() == id || u.getName() == name) {
            cout << "User already exists!" << endl;
            return;
        }
    }
    users.push_back(User(id, name, password));
    cout << "Registration successful!" << endl;
}

int login(vector<User> &users, string &loggedInUser) {
    string name, password;
    cout << "Enter username: ";
    cin >> name;
    cout << "Enter password: ";
    cin >> password;
    for (auto &u : users) {
        if (u.getName() == name && u.getPassword() == password) {
            loggedInUser = name;
            cout << "Login successful!" << endl;
            return u.getId();
        }
    }
    cout << "Invalid credentials!" << endl;
    return -1;
}

void RailwaySystem() {
    int choice1;
    do {
        cout << "\n1. Register\n2. Login\n3. Exit\nEnter your choice: ";
        cin >> choice1;
        if (choice1 == 1) {
            registerUser(users);
        } else if (choice1 == 2) {
            string loggedInUser;
            int userId = login(users, loggedInUser);
            if (userId != -1) {
                int choice2;
                do {
                    cout << "\n1. View Available Trains\n2. Book Train\n3. View My Bookings\n4. Logout\nEnter your choice: ";
                    cin >> choice2;
                    if (choice2 == 1) {
                        for (auto &t : availableTrains) t.display();
                    } else if (choice2 == 2) {
                        int tid;
                        cout << "Enter Train ID to book: ";
                        cin >> tid;
                        Booking* res = new Reservation(loggedInUser);
                        res->displayBookingDetails(); 
                        res->calculateFare();
                        res->bookTrain(tid, availableTrains);
                        res->displayBookingDetails(); 
                        for (int i = 0; i < users.size(); ++i) {
                            if (users[i].getId() == userId) {
                                users[i].addBooking(tid, tid);
                            }
                        }
                        delete res;
                    } else if (choice2 == 3) {
                        for (auto &u : users) {
                            if (u.getId() == userId) u.viewBookings();
                        }
                    }
                } while (choice2 != 4);
            }
        }
    } while (choice1 != 3);
}

int main() {
    initializeTrains();
    RailwaySystem();
    return 0;
}