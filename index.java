import java.security.Principal;
import java.util.Scanner;

// props individual cell
class cell {
    boolean gold = false;
    boolean smell = false;
    boolean wumpus = false;
    boolean wind = false;
    boolean hole = false;
}

class player {
    int x = 0;
    int y = 0;
    int arrows = 0;
    boolean hasGold = false;
    String direction = "E";

    public player (int col, int row, int ammo, String facing) {
        x = col;
        y = row;
        arrows = ammo;
        direction = facing;
    }

    public void turnLeft () {
        if (direction.equals("N")) {
            direction = "W";
        } else if (direction.equals("E")) {
            direction = "N";
        } else if (direction.equals("S")) {
            direction = "E";
        } else if (direction.equals("W")) {
            direction = "S";
        }
    }
    public void turnRight () {
        if (direction.equals("N")) {
            direction = "E";
        } else if (direction.equals("E")) {
            direction = "S";
        } else if (direction.equals("S")) {
            direction = "W";
        } else if (direction.equals("W")) {
            direction = "N";
        }
    }
    public void move () {
        if (direction.equals("N")) {
            x--;
        }
        if (direction.equals("E")) {
            y++;
        }
        if (direction.equals("S")) {
            x++;
        }
        if (direction.equals("W")) {
            y--;
        }
    }
    public void setPosition (int col, int row) {
        x = col;
        y = row;
    }
    public boolean fire () {
        if (arrows <= 0) {
            return false;
        } else {
            arrows--;
            return true;
        }
    }


}

class index {

    //get random
    static int random (int max){
        return (int)(Math.random()*max);
    }

    // init board
    static cell[][] initBoard(int cols, int rows){
        cell[][] columns = new cell[cols][rows];
        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows; j++){
                columns[i][j] = new cell();
            }
    
        }
        return columns;
    }

    // setup board
    // TODO: Evitar que se cree un elemento en la casilla de partida del jugador
    static cell[][] setupBoard(cell[][] board, int holes, int wumpus, int gold) {
        int cols = board.length;
        int rows = board[0].length;
        // set holes
        for (int i = 0; i < holes; i++) {
            int col = random(cols);
            int row = random(rows);
            if (board[col][row].hole == true){
                i--;
            } else {
                board[col][row].hole = true;
    
                // set wind for hole
                if (col+1 < cols){ board[col+1][row].wind = true; }
                if (col-1 >= 0){ board[col-1][row].wind = true; }
                if (row+1 < rows){ board[col][row+1].wind = true; }
                if (row-1 >= 0){ board[col][row-1].wind = true; }
            }
        }

        // set wumpus
        for (int i = 0; i < wumpus; i++) {
            int col = random(cols);
            int row = random(rows);
            if (board[col][row].hole == true || board[col][row].wumpus == true){
                i--;
            } else {

                board[col][row].wumpus = true;

                // set wind for hole
                if (col+1 < cols){ board[col+1][row].smell = true; }
                if (col-1 >= 0){ board[col-1][row].smell = true; }
                if (row+1 < rows){ board[col][row+1].smell = true; }
                if (row-1 >= 0){ board[col][row-1].smell = true; }
            }
        }

        // set gold
        for (int i = 0; i < gold; i++) {
            int col = random(cols);
            int row = random(rows);
            if (board[col][row].hole != true){
                board[col][row].gold = true;
            } else {
                i--;
            }
        }
        return board;
    }

    static void printBoard(cell[][] board, String option) {
        int cols = board.length;
        int rows = board[0].length;
        for (int i = 0; i < cols; i++){
            for (int j = 0; j < rows; j++){
                if (option.equals("Wumpus")){
                    if ( board[i][j].wumpus == true ) {
                        System.out.print(" W ");
                    } else if ( board[i][j].smell == true ) {
                        System.out.print(" S ");
                    } else {
                        System.out.print(" - ");
                    }
                }
                if (option.equals("Holes")){
                    if ( board[i][j].hole ) {
                        System.out.print(" H ");
                    } else if ( board[i][j].wind ) {
                        System.out.print(" W ");
                    } else {
                        System.out.print(" - ");
                    }
                }
                if (option.equals("Gold")){
                    if ( board[i][j].gold ) {
                        System.out.print(" G ");
                    } else {
                        System.out.print(" - ");
                    }
                }
                // System.out.print("|");
            }
            System.out.println("");
        }
    }

    static void cls() {
        for (int i = 0; i <= 50; i++) {
            System.out.println("");
        }
    }

    static void printHelp() {
        System.out.println("'ayuda': muestra esta pantalla");
        System.out.println("'salir': sale de la aplicación");
        System.out.println("'ver': muestra la descripción de la sala");
        System.out.println("'avanzar': avanzas una casilla en la dirección en la que miras");
        System.out.println("'girar derecha': giras 90º a tu derecha");
        System.out.println("'girar izquierda': giras 90º a tu izquierda");
        System.out.println("'disparar': dispara una flecha en la dirección en la que miras");
        System.out.println("'recoger': coges el lingote del suelo");
    }
    static void printRoomDesc(player jugador, cell[][] tablero, int cols, int rows) {
        System.out.println("Te encuentras en la habitación [" + jugador.x + ","+ jugador.y +"] y miras hacia el "+ jugador.direction);
        if (jugador.y == 0){
            System.out.println("Hay una pared al Oeste");
        }
        if (jugador.x == cols -1){
            System.out.println("Hay una pared al Sur");
        }
        if (jugador.y == rows -1){
            System.out.println("Hay una pared al Este");
        }
        if (jugador.x == 0){
            System.out.println("Hay una pared al Norte");
        }
        if (tablero[jugador.x][jugador.y].wumpus){
            System.out.println("Oh! No! Has aparecido en la habitación del Wumpus!");
        }
        if (tablero[jugador.x][jugador.y].smell){
            System.out.println("Sientes un fuerte hedor, el Wumpus no debe andar lejos");
        }
        if (tablero[jugador.x][jugador.y].hole){
            System.out.println("Conforme pones un pié en la sala resbalas y caes al vacio");
        }
        if (tablero[jugador.x][jugador.y].wind){
            System.out.println("Sientes una gélida brisa en tu piel, puede que haya un pozo cerca.");
        }
        if (tablero[jugador.x][jugador.y].gold){
            System.out.println("Ves el reflejo de algo brillante a tua pies. ¡Un Lingote de ORO!");
        }
    }
    static boolean checkHit(player jugador, cell[][] tablero, int cols, int rows) {
        if (jugador.direction.equals("N")){
            for (int i = jugador.x; i>=0; i--){
                if (tablero[i][jugador.y].wumpus == true){
                    tablero[i][jugador.y].wumpus = false;
                    return true;
                }
            }
        }
        if (jugador.direction.equals("S")){
            for (int i = jugador.x; i < cols; i++){
                if (tablero[i][jugador.y].wumpus == true){
                    tablero[i][jugador.y].wumpus = false;
                    return true;
                }
            }
        }
        if (jugador.direction.equals("W")){
            for (int i = jugador.y; i>=0; i--){
                if (tablero[jugador.x][i].wumpus == true){
                    tablero[i][jugador.y].wumpus = false;
                    return true;
                }
            }
        }
        if (jugador.direction.equals("E")){
            for (int i = jugador.y; i < rows; i++){
                if (tablero[jugador.x][i].wumpus == true){
                    tablero[jugador.x][i].wumpus = false;
                    return true;
                }
            }
        }
        return false;
    }
    static String getCondition(player jugador, cell[][] tablero) {
        if (tablero[jugador.x][jugador.y].wumpus) {
            return "LoseW";
        }else if ( tablero[jugador.x][jugador.y].hole) {
            return "LoseH";
        } else if (jugador.hasGold) {
            return "Win";
        } else {
            return "Continue";
        }
    }
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        String userInput = "";
        int userInt;

        System.out.println("Bienvenido a Wumpus!!!");
        System.out.println("");
        // user defined
        int arrows = 1;
        int holes = 3;
        int cols = 5;
        int rows = 5;
        
        
        // game defined
        int wumpus = 1;
        int gold = 1;
        
        System.out.println("Primero vamos a configurar el juego");
        System.out.println("");
        System.out.println("1- Introduce el número de columnas (1,2,3,...):");
        userInt = input.nextInt();
        if (userInt != 0) {
            cols = userInt;
        } else {
            System.out.println("utilizando valor por defecto: " + cols);
        }

        System.out.println("2- Introduce el número de filas (1,2,3,...):");
        userInt = input.nextInt();
        if (userInt != 0) {
            rows = userInt;
        } else {
            System.out.println("utilizando valor por defecto: " + rows);
        }

        System.out.println("3- Introduce el número de pozos (1,2,3,...):");
        userInt = input.nextInt();
        if (userInt != 0) {
            holes = userInt;
        } else {
            System.out.println("utilizando valor por defecto: " + holes);
        }

        System.out.println("4- Introduce el número de flechas disponibles (1,2,3,...):");
        userInt = input.nextInt();
        if (userInt != 0) {
            arrows = userInt;
        } else {
            System.out.println("utilizando valor por defecto: " + arrows);
        }

        System.out.println("Dispones de " + arrows + " flecha/s");
        System.out.println("La mazmorra tiene un tamaño de [" + cols + "x" + rows +  "] y en ella encontrarás :");
        System.out.println("Wumpus: " + wumpus);
        System.out.println("Oro: " + gold);
        System.out.println("Pozos: " + holes);


        System.out.println("Pulsa cualquier tecla y luego 'enter' para continuar:");
        userInput = "";
        while (true) {
            userInput = input.nextLine();
            if (userInput.equals("")) {}
            else { 
                break;
            }
        }
        cls();

        // init board
        cell tablero[][] = initBoard(cols, rows);
        setupBoard(tablero, holes, wumpus, gold);
        player jugador = new player(0,0,arrows, "E");


        // Juego
        String condition = "Continue";
        while (condition.equals("Continue")) {

            condition = getCondition(jugador, tablero);
            if (condition.equals("Win") || condition.equals("LoseW") || condition.equals("LoseH")) {
                break;
            }

            System.out.println("Escribe tu próxima acción ('salir' para salir, 'ayuda' para lista de comandos)");
            printRoomDesc(jugador, tablero, cols, rows);
            userInput = input.nextLine();

            if (userInput.equals("salir")) {
                break;
            } else if (userInput.equals("mapa")) {
                cls();
                System.out.println("Mostrando el Mapa:");
                System.out.println("Tamaño: " + cols + "," + rows );
                printBoard(tablero, "Wumpus");
                System.out.println("");
                printBoard(tablero, "Holes");
                System.out.println("");
                printBoard(tablero, "Gold");
            } else if (userInput.equals("ayuda")) {
                cls();
                printHelp();
            } else if (userInput.equals("recoger")) {
                cls();
                if (tablero[jugador.x][jugador.y].gold) {
                    tablero[jugador.x][jugador.y].gold = false;
                    jugador.hasGold = true;
                    System.out.println("Has recogido el lingote de oro");
                } else {
                    System.out.println("No hay nada que recoger en el suelo");
                }
            } else if (userInput.equals("girar derecha")) {
                cls();
                jugador.turnRight();
            } else if (userInput.equals("girar izquierda")) {
                cls();
                jugador.turnLeft();
            } else if (userInput.equals("avanzar")) {
                cls();
                if (jugador.y == 0 && jugador.direction.equals("W")){
                    System.out.println("Hay una pared al Oeste, no te puedes mover!");
                } else if (jugador.x == cols -1 && jugador.direction.equals("S")){
                    System.out.println("Hay una pared al Sur, , no te puedes mover!");
                } else if (jugador.y == rows -1 && jugador.direction.equals("E")){
                    System.out.println("Hay una pared al Este, , no te puedes mover!");
                } else if (jugador.x == 0 && jugador.direction.equals("N")){
                    System.out.println("Hay una pared al Norte, , no te puedes mover!");
                } else {
                    jugador.move();
                }
            } else if (userInput.equals("disparar")) {
                cls();
                if (jugador.fire()) {
                    System.out.println("Dispara tu flecha en la dirección marcada esperando abatir a tu presa...");
                    if (checkHit(jugador, tablero, cols, rows)) {
                        System.out.println("Lo has conseguido! Has derribado al Wumpus! Enorabuena! Del Wumpus ahora sólo queda su hedor");
                    } else {
                        System.out.println("Escuchas como la flecha se estrella contra la pared. Parece que el wumpus no estaba en esa dirección");
                    }
                } else {
                    System.out.println("No te quedan flechas");
                }
                
            }
            else {
                cls();
                System.out.println("Comando incorrecto, escribe 'ayuda' para ver todos los comandos disponibles");
            }
            
        }

        if (condition.equals("Win")){
            cls();
            System.out.println("Enhorabuena te has hecho con el botín!");
            System.out.println("Has ganado el Juego!");
        }
        if (condition.equals("LoseW")){
            System.out.println("Has entradoen la habitación del Wumpus y bueno, no ha sido agradable");
            System.out.println("Game Over");
        }
        if (condition.equals("LoseH")){
            System.out.println("Al entrar en la habitación resbalas y caes. Todavía sigues cayendo...");
            System.out.println("Game Over");
        }

    }
}