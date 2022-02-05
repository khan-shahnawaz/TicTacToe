import java.util.Scanner; // For taking User Input

public class TicTacToe
{
    static Scanner sc= new Scanner(System.in); // sc variable to take input from the Players
    public static class game // game Class having useful methods like begin and display instructions
    {
        public void DisplayInstructions() // Just to Print instructions for the manual player
        {
            System.out.println("");
            System.out.println("When asked to move, the player will input the coordinates seperate by space and press enter. First number denotes the row number(Numbered from top to bottom) and Second number dentotes Column(Numbered from left to right)\n");
        }
        public void Begin(player Player1, player Player2) // Begins the game by taking moves from the player
        {
            this.DisplayInstructions();
            Board myBoard=new Board();
            int MoveCount=1; // Stores current move count
            myBoard.Print();
            while (MoveCount<10) // Total 9 moves will be there
            {
                if (MoveCount%2==1) // Player1 will move in odd movecount
                {
                    coordinate PlayerInput = Player1.Move(myBoard,MoveCount,Player1,Player2); // Coordinates for taking input from player
                    if(this.Validate(myBoard, PlayerInput)) //Checking if the user made correct move
                    {
                        myBoard.cell[PlayerInput.row-1][PlayerInput.column-1]=Player1.symbol; // Putting symbol on the board
                        myBoard.Print(); // Printing the board after the move
                        System.out.println(Player1.name+" plays "+PlayerInput.row+" "+PlayerInput.column+"\n"); // Display which move the last player made
                        if (myBoard.CheckWin(Player1,Player2)) // Checking if this player won
                        {
                            Player1.Greet(); // Greeting the player for their win
                            this.GameOver(); // Game is not over
                            return;
                        }
                    }
                    else
                    {
                        System.out.println("Invalid Input! Try Again"); // In case user made invalid move
                        continue;
                    }
                }
                else 
                { // In even move count second player will move in the same way as of the player1
                    coordinate PlayerInput = Player2.Move(myBoard,MoveCount,Player1,Player2);
                    if(this.Validate(myBoard, PlayerInput))
                    {
                        myBoard.cell[PlayerInput.row-1][PlayerInput.column-1]=Player2.symbol;
                        myBoard.Print();
                        System.out.println(Player2.name+" plays "+PlayerInput.row+" "+PlayerInput.column+"\n");
                        if (myBoard.CheckWin(Player1,Player2))
                        {
                            Player2.Greet();
                            this.GameOver();
                            return;
                        }
                    }
                    else
                    {
                        System.out.println("Invalid Input! Try Again");
                        continue;
                    }
                }
                MoveCount++;
            }
            System.out.println("It's a Draw!"); // When nobody won and moves are over it's a draw
            this.GameOver();
        }
        public void GameOver() // Just for printing gameover messesge
        {
            System.out.println("");
            System.out.println("------------------GAME OVER------------------");
        }
        public boolean Validate(Board myBoard, coordinate InputCoordinate) // Function to check if a move is correct or not
        {
            if (InputCoordinate.row>3 || InputCoordinate.row<1 || InputCoordinate.column>3 || InputCoordinate.column<1)
            { // Checking if a player entered correct coordinate.
                return false;
            }
            if (myBoard.cell[InputCoordinate.row-1][InputCoordinate.column-1]==' ')
            { // Checking if the position is currently vacant
                return true;
            }
            else
            {
                return false;
            }
        }

    }
    public static class player // Just a dummy parent parent class whose children are person and commputer
    {
        String name; // Player will have its name
        char symbol; // Player's symbol (X or O or anything else)
        public coordinate Move(Board myboard,int MoveCount, player player1, player player2) // just null functions to be extended into child
        {
            return null;
        }
        public void Greet()
        {

        }

    }
    public static class person extends player // person class
    {
        person(String name, char symbol) // Constructor for the person class
        {
            this.name=name;
            this.symbol=symbol;
        }
        public coordinate Move(Board myboard,int move, player player1, player player2) // For taking input from the person through standard Input
        {
            System.out.println(this.name+"'s Turn!");
            coordinate NewInput= new coordinate();
            NewInput.row= sc.nextInt();
            NewInput.column=sc.nextInt();
            return NewInput;
        }
        public void Greet() // Greeting the person
        {
            System.out.println("Congratulations! "+this.name+". You have won the game.");
        }
    }
    public static class computer extends player
    {
        computer(String name, char symbol) // Constructor for the Computer
        {
            this.name=name;
            this.symbol=symbol;
        }
        public coordinate Move(Board myboard,int move,player player1, player player2)
        { // Method to make computer move by optimal strategy
            coordinate NewInput= new coordinate(); // Coordintate to return
            NewInput.row=1;
            NewInput.column=1;
            int curmax=-2;
            int curmin=2;
            int temp;
            for (int row=0;row<3;row++) // Exploring all possible moves for the compter to calculate which one is optimal
            {
                for (int column=0;column<3;column++)
                {
                    if (myboard.cell[row][column]!=' ')
                    {
                        continue;
                    }
                    if (move%2==1) // If computer plays first it will try to maximise their score. However in this program computer will always play second
                    {
                        myboard.cell[row][column]=this.symbol;
                        temp=MinMax(myboard, move+1, false,player1,player2); // Calling minimax algorithm for best move
                        myboard.cell[row][column]=' ';
                        if (temp>curmax) // If the current move maximises score for the computer
                        {
                            NewInput.row=row+1;
                            NewInput.column=column+1;
                            curmax=temp;
                        }
                    }
                    else
                    {
                        myboard.cell[row][column]=this.symbol;
                        temp=MinMax(myboard, move+1, true,player1, player2);
                        myboard.cell[row][column]=' ';
                        if (temp<curmin) // Minimising the score if the compter plays second
                        {
                            NewInput.row=row+1;
                            NewInput.column=column+1;
                            curmin=temp;
                        }
                    }
                }
            }
            return NewInput;
        }
        public void Greet()
        {
            System.out.println("You lose!"); // Computer don't need greeting messege. Just told the human that they lose.
        }
        public int MinMax(Board myboard,int Move,boolean isMax,player player1,player player2)
        { // Implementaion of minimax algorithm for finding the best move
            if (myboard.CheckWin(player1,player2)) // If the board is in winnging position
            {
                if (Move%2==0) // If its second player's turn, first player won and maximises the score to 1
                {
                    return 1;
                }
                else // If its first player's turn, second player won the game and hence minimises the score
                {
                    return -1;
                }
            }  
            if (Move==10) // If all moves are over, its a draw
            {
                return 0;
            }
            if (isMax) // If the current user is trying to maximise the score
            {
                int Curmax=-2;
                int Temp;
                for (int row=0;row<3;row++)
                {
                    for (int column=0;column<3;column++)
                    {
                        if (myboard.cell[row][column]==' ')
                        {
                            if (Move%2==1)
                            {
                                myboard.cell[row][column]=player1.symbol;
                            }
                            else
                            {
                                myboard.cell[row][column]=player2.symbol;
                            }
                            Temp= MinMax(myboard, Move+1, !isMax,player1,player2);
                            myboard.cell[row][column]=' ';
                            if (Temp>Curmax)
                            {
                                Curmax=Temp;
                            }
                        }
                    }
                }
                return Curmax;
            }
            else // If the player is trying to minimise the score
            {
                int Curmin=2;
                int Temp;
                for (int row=0;row<3;row++)
                {
                    for (int column=0;column<3;column++)
                    {
                        if (myboard.cell[row][column]==' ')
                        {
                            if (Move%2==1)
                            {
                                myboard.cell[row][column]=player1.symbol;
                            }
                            else
                            {
                                myboard.cell[row][column]=player2.symbol;
                            }
                            Temp= MinMax(myboard, Move+1, !isMax,player1,player2);
                            myboard.cell[row][column]=' ';
                            if (Temp<Curmin)
                            {
                                Curmin=Temp;
                            }
                        }
                    }
                }
                return Curmin;
            }
        }
    }
    public static class Board // Board class which stores the grid and have methods to check winning configuration
    {
        char[][] cell;
        Board() // Constructor which initialises all the cells with blank space
        {
            this.cell = new char[3][3];
            for (int row=0;row<3;row++)
            {
                for(int column=0;column<3;column++)
                {
                    this.cell[row][column]=' ';
                }
            }
        }
        public void Print() // To print the grid
        {
            for (int row=0;row<3;row++)
            {
                System.out.println(" ----------- "); //Printing upper border
                System.out.print("| ");
                for(int column=0;column<3;column++)
                {
                    System.out.print(cell[row][column]+" | "); //Printing side borders
                }
                System.out.println();
            }
            System.out.println(" ----------- "); // Lower border of each row
        }
        public boolean CheckWin(player player1, player player2) // Checks if the current configuration is winning or nor
        {
            int Xcount,Ocount;
            for (int row=0;row<3;row++) // Checking all the rows for same symbol
            {
                Xcount=0;
                Ocount=0;
                for (int column=0;column<3;column++)
                {
                    if (this.cell[row][column]==player1.symbol)
                    {
                        Xcount++;
                    }
                    if (this.cell[row][column]==player2.symbol)
                    {
                        Ocount++;
                    }
                }
                if (Xcount==3 || Ocount==3)
                {
                    return true;
                }
            }
            for (int column=0;column<3;column++)
            { // Checking all the columns for the same symbol
                Xcount=0;
                Ocount=0;
                for (int row=0;row<3;row++)
                {
                    if (this.cell[row][column]==player1.symbol)
                    {
                        Xcount++;
                    }
                    if (this.cell[row][column]==player2.symbol)
                    {
                        Ocount++;
                    }
                }
                if (Xcount==3 || Ocount==3)
                {
                    return true;
                }
            }
            Xcount=0;
            Ocount=0;
            for (int row=0;row<3;row++) 
            {// Checking the first diagonal for same symbol
                int column=row;
                if (this.cell[row][column]==player1.symbol)
                {
                    Xcount++;
                }
                if (this.cell[row][column]==player2.symbol)
                {
                    Ocount++;
                }
                
            }
            if (Xcount==3 || Ocount==3)
                {
                    return true;
                }
            Xcount=0;
            Ocount=0;
            for (int row=0;row<3;row++)
            { // Checking the other diagonal for the same symbol
                
                int column=2-row;
                if (this.cell[row][column]==player1.symbol)
                {
                    Xcount++;
                }
                if (this.cell[row][column]==player2.symbol)
                {
                    Ocount++;
                }
            }
            if (Xcount==3 || Ocount==3)
                {
                    return true;
                }
            return false;
        }
    }
    public static class coordinate // A class to store coordinate
    {
        int row,column;
    }
    public static void main(String[] args)
    {
        game newGame= new game(); // Initialising new game
        person player1= new person("Player 1",'X'); // As per the requirement of the assignment player will always have X and no name has to be taken from the user. This field is created as an add on if we need same class in some other project
        person player2= new person("Player 2",'O');
        computer Computer = new computer("Computer",'O'); // Computer always players with O
        System.out.println("Welcome to the TicTacToe game.");
        System.out.println("");
        System.out.println("Enter 0 to play with Computer and 1 to player with other player");
        int choice= sc.nextInt();
        if (choice==1)
        {
            newGame.Begin(player1,player2); // Begin the game with two persons
        }
        else
        {
            newGame.Begin(player1, Computer); // Begin the game with computer
        }
    }
}
