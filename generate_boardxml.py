import argparse, sys

MAX_ROW_COL = 10

def generate_boardxml(row, col):
    # Fixed info
    info = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\
<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n\
    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n\
    xmlns:tools=\"http://schemas.android.com/tools\"\n\
    android:layout_width=\"match_parent\"\n\
    android:layout_height=\"match_parent\"\n\
    android:orientation=\"vertical\"\n\
    tools:context=\"com.codinginflow.tictactoe.MainActivity\">\n\
    <RelativeLayout\n\
        android:layout_width=\"match_parent\"\n\
        android:layout_height=\"wrap_content\">\n\
        <TextView\n\
            android:id=\"@+id/text_view_timer\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:textSize=\"30sp\" />\n\
        <TextView\n\
            android:id=\"@+id/text_view_remaining_mines\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:layout_below=\"@+id/text_view_timer\"\n\
            android:textSize=\"30sp\" />\n\
        <Button\n\
            android:id=\"@+id/button_reset\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:layout_alignParentEnd=\"true\"\n\
            android:layout_centerVertical=\"true\"\n\
            android:layout_marginEnd=\"33dp\"\n\
            android:text=\"reset\" />\n\
    </RelativeLayout>\n\
    <RelativeLayout\n\
        android:layout_width=\"match_parent\"\n\
        android:layout_height=\"match_parent\">\n\
        <TableLayout\n\
            android:layout_width=\"fill_parent\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:layout_marginLeft=\"15dp\"\n\
            android:layout_marginRight=\"15dp\"\n\
            android:layout_marginBottom=\"20dp\"\n\
            android:layout_centerInParent=\"true\">\n"
    # Info generated based on row and col
    for i in range(row):
        info += "           <TableRow\n\
                android:layout_width=\"match_parent\"\n\
                android:layout_height=\"0dp\"\n\
                android:weightSum=\"1\"\n\
                android:layout_weight=\"0.5\">\n"
        for j in range(col):
            info += "               <RelativeLayout\n\
                    android:layout_width=\"0dp\"\n\
                    android:layout_height=\"wrap_content\"\n\
                    android:layout_weight=\"%.2f\">\n\
                    <com.example.minesweeper.ResizableButton\n\
                        android:layout_width=\"wrap_content\"\n\
                        android:layout_height=\"0dp\"\n\
                        android:id=\"@+id/button_%02d%02d\"\n\
                        android:tag = \"%02d%02d\"\n\
                        android:gravity=\"center|bottom\"\n\
                        android:paddingBottom=\"10dp\"\n\
                        android:contentDescription=\"@string/game_board\"/>\n\
                </RelativeLayout>\n" % (1/row,i,j,i,j)
        info += "           </TableRow>\n\n"
    info += "       </TableLayout>\n\
    </RelativeLayout>\n\
</LinearLayout>"
    return info


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--row', type=int, required=True, help="num of rows")
    parser.add_argument('--col', type=int, required=True, help="num of columns")
    
    args = parser.parse_args()
    row = args.row
    col = args.col

    if row > MAX_ROW_COL or col > MAX_ROW_COL:
        raise ValueError("Error: row or col is above maximum constrant")

    # Write the file
    Filename = "app/src/main/res/layout/fragment_second_temp.xml"

    with open(Filename, 'w') as f:
        f.write(generate_boardxml(row,col))
        f.write('\n')

main()


