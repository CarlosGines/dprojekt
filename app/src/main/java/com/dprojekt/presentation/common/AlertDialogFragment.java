
package com.dprojekt.presentation.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dprojekt.R;

/**
 * Custom {@link DialogFragment} used to create dialogs properly and with
 * flexibility.
 */
public class AlertDialogFragment extends DialogFragment {

    // ==========================================================================
    // Constants
    // ==========================================================================

    /** Tag for DialogFragments */
    public static final String TAG = "alert_dialog_fragment_tag";

    /** Key for the message */
    public static final String REQUEST_CODE_KEY = "request_code";

    /** Key for the title res ID */
    public static final String TITLE_ID_KEY = "title_id";

    /** Key for the icon res ID */
    public static final String ICON_ID_KEY = "icon_id";

    /** Key for the message res ID */
    public static final String MESSAGE_ID_KEY = "message_id";

    /** Key for the message */
    public static final String MESSAGE_KEY = "message";

    /** Key for the number of buttons */
    public static final String NUM_BUTTONS_KEY = "num_buttons";

    /** Key for the name of button 1 */
    public static final String NAME_BUTTON_1_KEY = "name_button_1";

    /** Key for the name of button 2 */
    public static final String NAME_BUTTON_2_KEY = "name_button_2";


    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Instance of the interface to deliver action events */
    private AlertDialogListener mListener;

    // ==========================================================================
    // Pseudo-Constructor
    // ==========================================================================

	/*
	 * Every fragment must have an empty constructor, so it can be instantiated when restoring
	 * its activity's state. It is strongly recommended that subclasses do not have other
	 * constructors with parameters, since these constructors will not be called when the fragment
	 * is re-instantiated; instead, arguments can be supplied by the caller
	 * with setArguments(Bundle)
	 */

    /**
     * New instance for an AlertDialogFragment only with message and 2 buttons
     *
     * @param messageId   Resource ID of the message.
     * @param requestCode Private request code for the sender that will be
     *                    associated with the callback when it is invoked.
     * @param numButtons  Number of buttons, 1 for "OK", 2 for "OK and "Cancel".
     */
    public static AlertDialogFragment newInstance(int messageId, int numButtons,
                                                  int requestCode) {
        // Delegate
        return newInstance(0, 0, messageId, numButtons, requestCode, null);
    }

    /**
     * New instance for an AlertDialogFragment
     * 
     * @param titleId Resource ID of the title. Set 0 if no title.
     * @param iconId Resource ID of the icon. Set 0 if no icon.
     * @param messageId Resource ID of the message.
     * @param numButtons Number of buttons, 1 for "OK", 2 for "OK and "Cancel".
     * @param requestCode Private request code for the sender that will be
     *            associated with the callback when it is invoked.
     * @param nameButtons List of the new names for the buttons. Set null if no
     *            need to rename the buttons
     */
    public static AlertDialogFragment newInstance (int titleId, int iconId, int messageId,
            int numButtons, int requestCode, int[] nameButtons) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setArguments(titleId, iconId, messageId, null,
                numButtons, requestCode, nameButtons);
        // Used for dialog recreation on device rotation
        alertDialogFragment.setRetainInstance(true);
        return alertDialogFragment;
    }

    /**
     * New instance for an AlertDialogFragment
     * 
     * @param titleId Resource ID of the title. Set 0 if no title.
     * @param iconId Resource ID of the icon. Set 0 if no icon.
     * @param message The message.
     * @param numButtons Number of buttons, 1 for "OK", 2 for "OK and "Cancel".
     * @param requestCode Private request code for the sender that will be
     *            associated with the callback when it is invoked.
     */
    public static AlertDialogFragment newInstance (int titleId, int iconId, String message,
            int numButtons, int requestCode, int[] nameButtons) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setArguments(titleId, iconId, 0, message,
                numButtons, requestCode, nameButtons);
        // Used for dialog recreation on device rotation
        alertDialogFragment.setRetainInstance(true);
        return alertDialogFragment;
    }

    /** Set the arguments required to create an Alert Dialog Fragment */
    private void setArguments (int titleId, int iconId, int messageId, String message,
                               int numButtons, int requestCode, int[] nameButtons)  {
        Bundle args = new Bundle();
        args.putInt(REQUEST_CODE_KEY, requestCode);
        args.putInt(TITLE_ID_KEY, titleId);
        args.putInt(ICON_ID_KEY, iconId);
        args.putInt(MESSAGE_ID_KEY, messageId);
        args.putString(MESSAGE_KEY, message);
        args.putInt(NUM_BUTTONS_KEY, numButtons);
        if (nameButtons != null) {
            args.putInt(NAME_BUTTON_1_KEY, nameButtons[0]);
            if(numButtons > 1) {
                args.putInt(NAME_BUTTON_2_KEY, nameButtons[1]);
            }
        }
        setArguments(args);
    }

    // ==========================================================================
    // Fragment lifecycle methods
    // ==========================================================================

    // Override the Fragment.onAttach() method to instantiate the
    // AlertDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AlertDialogListener so we can send events to the
            // host
            mListener = (AlertDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AlertDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers according to parameters
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle args = getArguments();

        // Set dialog title
        int titleId = args.getInt(TITLE_ID_KEY);
        if (titleId != 0) {
            builder.setTitle(titleId);
        }

        // Set dialog icon
        int iconId = args.getInt(ICON_ID_KEY);
        if (iconId != 0) {
            builder.setIcon(iconId);
        }

        // Set dialog message
        int messageId = args.getInt(MESSAGE_ID_KEY);
        if (messageId != 0) {
            builder.setMessage(messageId);
        } else {
            builder.setMessage(args.getString(MESSAGE_KEY));
        }

        // Set positive button handler
        builder.setPositiveButton(args.getInt(NAME_BUTTON_1_KEY, R.string.OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the
                        // host activity
                        mListener.onDialogPositiveClick(args.getInt(REQUEST_CODE_KEY));
                    }
                });
        if (args.getInt(NUM_BUTTONS_KEY) > 1) {
            // Set negative button handler if more than 1 button.
            builder.setNegativeButton(args.getInt(NAME_BUTTON_2_KEY, R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send the negative button event back to the
                            // host activity
                            mListener.onDialogNegativeClick(args.getInt(REQUEST_CODE_KEY));
                        }
                    });
        }
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        // We use the same event for cancellation and the negative button
        mListener.onDialogNegativeClick(getArguments().getInt(REQUEST_CODE_KEY));
    }

    /**
     * We have to add this code to stop the dialog from being dismissed on
     * rotation, due to a bug with the compatibility library.
     *
     * @since 2014-11-09
     */
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    // ==========================================================================
    // Custom interface
    // ==========================================================================

    /**
     * The activity that creates an instance of AlertDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the request code that had been given.
     */
    public interface AlertDialogListener {
        void onDialogPositiveClick(int requestCode);
        void onDialogNegativeClick(int requestCode);
    }
}
